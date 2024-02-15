package pi.actors;

import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import pi.messages.CalculationRequestMessage;
import pi.messages.CalculationResponseMessage;
import pi.messages.IMessage;
import pi.messages.PIMessage;

public class PI extends AbstractBehavior<IMessage> {
    private long totalIterations;
    private long numActors;
    private long messageReceivedCounter = 0;
    private long isInsideCounter = 0;

    public PI(ActorContext<IMessage> context) {
        super(context);
    }

    @Override
    public Receive<IMessage> createReceive() {
        return newReceiveBuilder()
            .onMessage(PIMessage.class, this::estimate)
            .onMessage(CalculationResponseMessage.class, this::receiver)
            .build();
    }
    
    private Behavior<IMessage> estimate(PIMessage message) {
        this.numActors = message.getNumActors();
        this.totalIterations = message.getIterations();

        long pointsPerActor = (long) Math.floor((double)(this.totalIterations / message.getNumActors()));
        System.out.println("pontos por ator: "+ pointsPerActor);
        System.out.println("numero atores: "+ this.numActors);
        for (long i = 0; i < message.getNumActors(); i++) {
            String calculationActorName = "PIActor_" + i;
            System.out.println(calculationActorName);
            // ActorSystem<CalculationRequestMessage> calculationActor = ActorSystem.create(Randomize.start(), calculationActorName);
            ActorRef<CalculationRequestMessage> calculationActor = getContext().spawn(Randomize.start(), calculationActorName);

            CalculationRequestMessage calculationMessage = new CalculationRequestMessage(pointsPerActor, getContext().getSelf());

            calculationActor.tell(calculationMessage);
        }
/* 
        long rest = this.totalIterations % message.getNumActors();

        if (rest > 0) {
            this.numActors++;
            long actorNumber = message.getNumActors() + 1;
            String calculationActorName = "PIActor_" + actorNumber;
            ActorSystem<CalculationRequestMessage> calculationActor = ActorSystem.create(Randomize.start(), calculationActorName);

            CalculationRequestMessage calculationMessage = new CalculationRequestMessage(pointsPerActor, getContext().getSelf());

            calculationActor.tell(calculationMessage);
            calculationActor.terminate();
        }
*/
        return this;
    }

    private Behavior<IMessage> receiver(CalculationResponseMessage message) {
        this.messageReceivedCounter++;
        this.isInsideCounter += message.getIsInsideCounter();
        
        System.out.println("Message counter: " + this.messageReceivedCounter);

        if (this.messageReceivedCounter == this.numActors) {
            double estimatedPi = (4.0 * this.isInsideCounter) / this.totalIterations;

            String insideCircle = "Total dentro do circulo: "+ this.isInsideCounter + " para " + this.numActors + " atores";
            String estimadedValue = "Valor estimado de PI para " + this.totalIterations + " iteracoes foi " + estimatedPi;
            
            System.out.println(insideCircle);
            System.out.println(estimadedValue);
            
            return Behaviors.stopped();
        }

        return this;
    }


    public static Behavior<IMessage> start() {
        return Behaviors.setup(PI::new);
    }
}

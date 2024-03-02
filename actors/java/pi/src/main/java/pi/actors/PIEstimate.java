package pi.actors;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import pi.messages.CalculationRequestMessage;
import pi.messages.FinishMessage;
import pi.messages.IMessage;
import pi.messages.PIMessage;

public class PIEstimate extends AbstractBehavior<IMessage> {
    private long totalPoints;
    private long numActors;
    private long pointsPerActor = 0;

    public PIEstimate(ActorContext<IMessage> context) {
        super(context);
    }

    @Override
    public Receive<IMessage> createReceive() {
        return newReceiveBuilder()
            .onMessage(PIMessage.class, this::estimate)
            .onMessage(FinishMessage.class, this::finish)
            .build();
    }
    
    private Behavior<IMessage> estimate(PIMessage message) {
        this.numActors = message.getNumActors();
        this.totalPoints = message.getTotalPoints();
        this.pointsPerActor = (long) Math.floor((double)(this.totalPoints / this.numActors));

        String receiverActorName = "ReceiverActor";
        ActorRef<IMessage> receiverActor = getContext().spawn(PIReceiver.start(this.numActors, this.totalPoints, this.pointsPerActor), receiverActorName);

        for (long i = 0; i < message.getNumActors(); i++) {
            String calculationActorName = "MonteCarloActor_" + i;
            ActorRef<CalculationRequestMessage> calculationActor = getContext().spawn(MonteCarlo.start(), calculationActorName);

            CalculationRequestMessage calculationMessage = new CalculationRequestMessage(this.pointsPerActor, receiverActor, getContext().getSelf());

            calculationActor.tell(calculationMessage);
        }

        return this;
    }
        
    private Behavior<IMessage> finish(FinishMessage message) {
        return Behaviors.stopped();
    }

    public static Behavior<IMessage> start() {
        return Behaviors.setup(PIEstimate::new);
    }
}

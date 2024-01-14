package pi;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import pi.messages.CalculationRequest;

public class PI extends AbstractBehavior<CalculationRequest> {

    public PI(ActorContext<CalculationRequest> context) {
        super(context);
        //TODO Auto-generated constructor stub
    }

    @Override
    public Receive<CalculationRequest> createReceive() {
        return newReceiveBuilder().onMessage(CalculationRequest.class, this::estimate).build();
    }
    
    private Behavior<CalculationRequest> estimate(CalculationRequest message) {
        // getContext().getLog().info("Hello {}!", command.whom());
        //#greeter-send-message
        // command.replyTo().tell(new Greeted(command.whom(), getContext().getSelf()));
        //#greeter-send-message
        System.out.println("mensagem chegou");
        System.out.println(message.getIterations());
        return this;
    }

    public static Behavior<CalculationRequest> behavior() {
        return Behaviors.setup(PI::new);
    }
}

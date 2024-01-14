package pi;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.*;
import pi.messages.CalculationRequest;

public class Randomize extends AbstractBehavior<CalculationRequest> {
  public static Behavior<CalculationRequest> create() {
    return Behaviors.setup(Randomize::new);
  }

  public Randomize(ActorContext<CalculationRequest> context) {
    super(context);
  }

    @Override
    public Receive<CalculationRequest> createReceive() {
        return newReceiveBuilder()
            .onMessage(CalculationRequest.class, this::calculate)
            .build();
    }

    private Behavior<CalculationRequest> calculate(CalculationRequest message) {
        //#greeter-send-message
        // message.sender();
        // System.out.println(message);
        //#greeter-send-message
        return this;
    }

}

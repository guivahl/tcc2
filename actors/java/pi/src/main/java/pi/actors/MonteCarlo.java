package pi.actors;

import java.util.Random;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.*;
import pi.messages.CalculationRequestMessage;
import pi.messages.CalculationResponseMessage;

public class MonteCarlo extends AbstractBehavior<CalculationRequestMessage> {
  public static Behavior<CalculationRequestMessage> create() {
    return Behaviors.setup(MonteCarlo::new);
  }

  public MonteCarlo(ActorContext<CalculationRequestMessage> context) {
    super(context);
  }

    @Override
    public Receive<CalculationRequestMessage> createReceive() {
        return newReceiveBuilder()
            .onMessage(CalculationRequestMessage.class, this::calculate)
            .build();
    }

    private Behavior<CalculationRequestMessage> calculate(CalculationRequestMessage message) {
        long isInsideCounter = 0;

        Random random = new Random();

        for (long i = 0; i < message.getPoints(); i++) {
            double x = random.nextDouble();
            double y = random.nextDouble();
            boolean isInside = x * x + y * y <= 1.0;

            if (isInside) isInsideCounter++;
        }

        CalculationResponseMessage responseMessage = new CalculationResponseMessage(isInsideCounter, message.getMainActor());

        message.getSender().tell(responseMessage);

        return this;
    }

    public static Behavior<CalculationRequestMessage> start() {
        return Behaviors.setup(MonteCarlo::new);
    }
}

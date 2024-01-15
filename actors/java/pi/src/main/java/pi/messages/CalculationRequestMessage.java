package pi.messages;

import akka.actor.typed.ActorRef;

public class CalculationRequestMessage implements IMessage {
    private int iterations;
    private ActorRef<IMessage> sender;

    public CalculationRequestMessage(int iterations, ActorRef<IMessage> actorRef) {
        this.iterations = iterations;
        this.sender = actorRef;
    }

    public ActorRef<IMessage> getSender() {
        return this.sender;
    }

    public int getIterations() {
        return this.iterations;
    }
}

package pi.messages;

import akka.actor.typed.ActorRef;

public class CalculationRequestMessage implements IMessage {
    private long iterations;
    private ActorRef<IMessage> sender;

    public CalculationRequestMessage(long iterations, ActorRef<IMessage> actorRef) {
        this.iterations = iterations;
        this.sender = actorRef;
    }

    public ActorRef<IMessage> getSender() {
        return this.sender;
    }

    public long getIterations() {
        return this.iterations;
    }
}

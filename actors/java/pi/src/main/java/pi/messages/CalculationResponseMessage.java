package pi.messages;

import akka.actor.typed.ActorRef;

public class CalculationResponseMessage implements IMessage {
    private long isInsideCounter;
    private ActorRef<IMessage> sender;

    public CalculationResponseMessage(long isInsideCounter, ActorRef<IMessage> actorRef) {
        this.isInsideCounter = isInsideCounter;
        this.sender = actorRef;
    }

    public long getIsInsideCounter() {
        return this.isInsideCounter;
    }

    public ActorRef<IMessage> getSender() {
        return this.sender;
    }
}

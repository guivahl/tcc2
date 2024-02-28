package pi.messages;

import akka.actor.typed.ActorRef;

public class CalculationRequestMessage implements IMessage {
    private long points;
    private ActorRef<IMessage> sender;

    public CalculationRequestMessage(long points, ActorRef<IMessage> actorRef) {
        this.points = points;
        this.sender = actorRef;
    }

    public ActorRef<IMessage> getSender() {
        return this.sender;
    }
    
    public long getPoints() {
        return this.points;
    }
}

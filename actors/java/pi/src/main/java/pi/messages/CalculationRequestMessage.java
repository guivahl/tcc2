package pi.messages;

import akka.actor.typed.ActorRef;

public class CalculationRequestMessage implements IMessage {
    private long points;
    private ActorRef<IMessage> sender;
    private ActorRef<IMessage> mainActor;

    public CalculationRequestMessage(long points, ActorRef<IMessage> actorRef, ActorRef<IMessage> mainActorRef) {
        this.points = points;
        this.sender = actorRef;
        this.mainActor = mainActorRef;
    }

    public ActorRef<IMessage> getSender() {
        return this.sender;
    }
    
    public ActorRef<IMessage> getMainActor() {
        return this.mainActor;
    }
    
    public long getPoints() {
        return this.points;
    }
}

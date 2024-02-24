package imageProcessor.messages;

import akka.actor.typed.ActorRef;

public class JoinMessage implements IMessage {
    private ActorRef<IMessage> sender;
    private int width;
    private int height;
    private int splitWidth;
    private int splitHeight;

    public JoinMessage(ActorRef<IMessage> sender, int width, int height, int splitWidth, int splitHeight) {
        this.sender = sender;
        this.width = width;
        this.height = height;
        this.splitWidth = splitWidth;
        this.splitHeight = splitHeight;
    }

    public ActorRef<IMessage> getSender() {
        return this.sender;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }
    
    public int getSplitWidth() {
        return this.splitWidth;
    }

    public int getSplitHeight() {
        return this.splitHeight;
    }
}

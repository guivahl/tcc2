package imageProcessor.messages;

import java.awt.image.BufferedImage;

import akka.actor.typed.ActorRef;

public class CountMessage implements IMessage {
    private ActorRef<IMessage> sender;
    private BufferedImage rotatedImage;
    private int order;

    public CountMessage (ActorRef<IMessage> sender, BufferedImage rotatedImage, int order) {
        this.sender = sender;
        this.rotatedImage = rotatedImage;
        this.order = order;
    }

    public BufferedImage getRotatedImage() {
        return this.rotatedImage;
    }

    public int getOrder() {
        return this.order;
    }

    public ActorRef<IMessage> getSender() {
        return this.sender;
    }
}

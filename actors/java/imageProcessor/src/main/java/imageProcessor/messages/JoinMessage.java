package imageProcessor.messages;

import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.TreeMap;

import akka.actor.typed.ActorRef;

public class JoinMessage implements IMessage {
    private ActorRef<IMessage> sender;
    private int width;
    private int height;
    private int splitWidth;
    private int splitHeight;
    private Map<Integer, BufferedImage> rotatedImages = new TreeMap<Integer, BufferedImage>();

    public JoinMessage(ActorRef<IMessage> sender, int width, int height, int splitWidth, int splitHeight, Map<Integer, BufferedImage> rotatedImages) {
        this.sender = sender;
        this.width = width;
        this.height = height;
        this.splitWidth = splitWidth;
        this.splitHeight = splitHeight;
        this.rotatedImages = rotatedImages;
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
    
    public Map<Integer, BufferedImage> getRotatedImages() {
        return this.rotatedImages;
    }
}

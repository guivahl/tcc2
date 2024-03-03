package imageProcessor.messages;

import java.awt.image.BufferedImage;

import akka.actor.typed.ActorRef;

public class SplitAndSaveMessage implements IMessage {
    private BufferedImage inputImage; 
    private int order;
    private int xStart; 
    private int yStart; 
    private int chunkWidth; 
    private int chunkHeight; 
    private String outputFilename; 
    private ActorRef<IMessage> sender;

    public SplitAndSaveMessage(
        BufferedImage inputImage,
        int order,
        int xStart,
        int yStart,
        int chunkWidth,
        int chunkHeight,
        String outputFilename, 
        ActorRef<IMessage> actorRef
    ) {
        this.inputImage = inputImage;
        this.order = order;
        this.xStart = xStart;
        this.yStart = yStart;
        this.chunkWidth = chunkWidth;
        this.chunkHeight = chunkHeight;
        this.outputFilename = outputFilename;
        this.sender = actorRef;
    }

    public BufferedImage getInputImage() {
        return this.inputImage;
    }

    public int getOrder() {
        return this.order;
    }

    public int getXStart() {
        return this.xStart;
    }

    public int getYStart() {
        return this.yStart;
    }

    public int getChunkWidth() {
        return this.chunkWidth;
    }

    public int getChunkHeight() {
        return this.chunkHeight;
    }

    public String getOutputPath() {
        return this.outputFilename;
    }
    
    public ActorRef<IMessage> getSender() {
        return this.sender;
    }

}

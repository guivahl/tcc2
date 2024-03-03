package imageProcessor.actors;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import imageProcessor.messages.FinishSaveMessage;
import imageProcessor.messages.IMessage;
import imageProcessor.messages.SplitAndInvertMessage;
import imageProcessor.messages.SplitAndSaveMessage;

public class ImageProcessor extends AbstractBehavior<IMessage> {
    private int splitWidth;
    private int splitHeight;
    private int width;
    private int height;
    

    public ImageProcessor(ActorContext<IMessage> context) {
        super(context);
    }

    @Override
    public Receive<IMessage> createReceive() {
        return newReceiveBuilder()
            .onMessage(SplitAndInvertMessage.class, this::splitAndInvertImage)
            .onMessage(FinishSaveMessage.class, this::finish)
            .build();
    }
    
    private Behavior<IMessage> splitAndInvertImage(SplitAndInvertMessage message) {
        this.splitHeight = message.getSplitHeight();
        this.splitWidth = message.getSplitWidth();

        try {
            File inputFile = new File(message.getInputPath());
            BufferedImage originalImage = ImageIO.read(inputFile);

            this.width = originalImage.getWidth();
            this.height = originalImage.getHeight();

            int chunkWidth = originalImage.getWidth() / this.splitWidth;
            int chunkHeight = originalImage.getHeight() / this.splitHeight;

            String counterActorName = "CounterActor";
            ActorRef<IMessage> counterActor = getContext().spawn(Counter.start(this.splitHeight * this.splitHeight, this.width, this.height, this.splitWidth, this.splitHeight, getContext().getSelf()), counterActorName);
    
            for(int i = 0; i < this.splitHeight; i++){
                for(int j = 0; j < this.splitWidth; j++){
                    String outputFilename =  String.format("tmp/rotated_%d_%d.png", i, j);

                    int xStart = j * chunkWidth;
                    int yStart = i * chunkHeight;
                    int order = j + (i * splitWidth);
;
                    String splitAndSaveActorName = "PIActor_" + i + "_" + j;
                    ActorRef<SplitAndSaveMessage> splitAndSaveActor = getContext().spawn(ImageSplitSave.start(), splitAndSaveActorName);

                    SplitAndSaveMessage splitAndSaveMessage = new SplitAndSaveMessage(
                        originalImage,
                        order,
                        xStart,
                        yStart,
                        chunkWidth,
                        chunkHeight,
                        outputFilename,
                        counterActor
                    );
        
                    splitAndSaveActor.tell(splitAndSaveMessage);
                }
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return this;
    }
    
    private Behavior<IMessage> finish(FinishSaveMessage message) {
        return Behaviors.stopped();
    }
    
    public static Behavior<IMessage> start() {
        return Behaviors.setup(ImageProcessor::new);
    }
}

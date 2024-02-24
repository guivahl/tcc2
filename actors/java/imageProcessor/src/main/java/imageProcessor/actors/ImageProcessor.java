package imageProcessor.actors;

import imageProcessor.*;
import imageProcessor.messages.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import javax.imageio.ImageIO;
import java.time.*;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import imageProcessor.messages.CountMessage;
import imageProcessor.messages.FinishSaveMessage;
import imageProcessor.messages.IMessage;
import imageProcessor.messages.JoinMessage;
import imageProcessor.messages.SplitAndInvertMessage;
import imageProcessor.messages.SplitAndSaveMessage;

public class ImageProcessor extends AbstractBehavior<IMessage> {
    private int counter = 0;
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
            .onMessage(CountMessage.class, this::count)
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

            for(int i = 0; i < this.splitHeight; i++){
                for(int j = 0; j < this.splitWidth; j++){
                    String outputFilename =  String.format("tmp/rotated_%d_%d.png", i, j);

                    int xStart = j * chunkWidth;
                    int yStart = i * chunkHeight;

                    String splitAndSaveActorName = "PIActor_" + i + "_" + j;
                    ActorRef<SplitAndSaveMessage> splitAndSaveActor = getContext().spawn(ImageSplitSave.start(), splitAndSaveActorName);
        
                    SplitAndSaveMessage splitAndSaveMessage = new SplitAndSaveMessage(
                        originalImage,
                        xStart,
                        yStart,
                        chunkWidth,
                        chunkHeight,
                        outputFilename,
                        getContext().getSelf()
                    );
        
                    splitAndSaveActor.tell(splitAndSaveMessage);
                }
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return this;
    }
    
    private Behavior<IMessage> count(CountMessage message) {
        this.counter++;

        if (this.counter == (this.splitWidth * this.splitHeight)) {
            String joinActorName = "PIActor_Join";

            ActorRef<JoinMessage> imageJoinActor = getContext().spawn(ImageJoin.start(), joinActorName);

            JoinMessage joinMessage = new JoinMessage(getContext().getSelf(), this.width, this.height, this.splitWidth, this.splitHeight);

            imageJoinActor.tell(joinMessage);
        }
    
        return this;
    }
    
    private Behavior<IMessage> finish(FinishSaveMessage message) {
        Instant stopTime = Instant.now();
        
        long timeExecutionMiliseconds = Duration.between(App.startTime, stopTime).toMillis();

        String csvString = String.format("%d,%d,%d,%s\n",
            this.splitWidth,
            this.splitHeight,
            timeExecutionMiliseconds, 
            message.getOutputPath()
        );
        
        try{
            Path path = Paths.get("results.csv");
            Files.write(path, csvString.getBytes(), StandardOpenOption.APPEND);
        } catch(Exception ex) {
            System.out.println("Erro ao escrever resultados no csv");
        }

        return Behaviors.stopped();
    }
    
    public static Behavior<IMessage> start() {
        return Behaviors.setup(ImageProcessor::new);
    }
}

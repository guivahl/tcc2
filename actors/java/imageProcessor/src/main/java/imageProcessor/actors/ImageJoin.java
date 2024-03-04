package imageProcessor.actors;

import imageProcessor.App;
import imageProcessor.messages.*;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Map;
import java.util.NavigableMap;

import javax.imageio.ImageIO;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import akka.util.Collections;
import imageProcessor.messages.FinishSaveMessage;
import imageProcessor.messages.JoinMessage;

public class ImageJoin extends AbstractBehavior<JoinMessage> {
    private int splitWidth;
    private int splitHeight;

    public ImageJoin(ActorContext<JoinMessage> context) {
        super(context);
    }

    @Override
    public Receive<JoinMessage> createReceive() {
        return newReceiveBuilder()
            .onMessage(JoinMessage.class, this::join)
            .build();
    }
    
    private Behavior<JoinMessage> join(JoinMessage message) {        
        this.splitHeight = message.getSplitHeight();
        this.splitWidth = message.getSplitWidth();

        BufferedImage combinedImage = new BufferedImage(message.getWidth(), message.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = combinedImage.createGraphics();

        int xOffset = 0;
        int yOffset = 0;

        for(Map.Entry<Integer,BufferedImage> rotatedImage :  ((NavigableMap<Integer, BufferedImage>) message.getRotatedImages()).descendingMap().entrySet()) {
            g2d.drawImage(rotatedImage.getValue(), xOffset, yOffset, null);
            xOffset += rotatedImage.getValue().getWidth();
            if (xOffset >= message.getWidth()) {
                xOffset = 0;
                yOffset += rotatedImage.getValue().getHeight();
            }
        } 

        g2d.dispose(); 

        String outputPath = "outputImages/final_" + System.currentTimeMillis() + ".png";

        try {
            ImageIO.write(combinedImage, "png", new File(outputPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Instant stopTime = Instant.now();
        
        long timeExecutionMiliseconds = Duration.between(App.startTime, stopTime).toMillis();

        int numberActors = this.splitWidth * this.splitHeight;

        String csvString = String.format("%d,%d,%d,%d,%s\n",
            numberActors,
            this.splitWidth,
            this.splitHeight,
            timeExecutionMiliseconds, 
            outputPath
        );
        
        try{
            Path path = Paths.get("results.csv");
            Files.write(path, csvString.getBytes(), StandardOpenOption.APPEND);
        } catch(Exception ex) {
            System.out.println("Erro ao escrever resultados no csv");
        }
        
        message.getSender().tell(new FinishSaveMessage());

        return this;
    }
    
    public static Behavior<JoinMessage> start() {
        return Behaviors.setup(ImageJoin::new);
    }
    
}

package imageProcessor.actors;

import imageProcessor.*;
import imageProcessor.messages.*;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
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
import imageProcessor.messages.CountMessage;
import imageProcessor.messages.SplitAndSaveMessage;

public class ImageSplitSave extends AbstractBehavior<SplitAndSaveMessage> {
    public ImageSplitSave(ActorContext<SplitAndSaveMessage> context) {
        super(context);
    }

    @Override
    public Receive<SplitAndSaveMessage> createReceive() {
        return newReceiveBuilder()
            .onMessage(SplitAndSaveMessage.class, this::splitAndSaveImage)
            .build();
    }
    
    private Behavior<SplitAndSaveMessage> splitAndSaveImage(SplitAndSaveMessage message) {
        try {
            BufferedImage subImage = message
                .getInputImage()
                .getSubimage(message.getXStart(), message.getYStart(), message.getChunkWidth(), message.getChunkHeight());

            AffineTransform transform = new AffineTransform();
            transform.rotate(Math.PI, message.getChunkWidth() / 2.0, message.getChunkHeight() / 2.0);

            BufferedImage rotatedImage = new BufferedImage(message.getChunkWidth(), message.getChunkHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics2D g = rotatedImage.createGraphics();
            g.drawImage(subImage, transform, null);
            g.dispose();

            File outputFile = new File(message.getOutputPath());
            ImageIO.write(rotatedImage, "jpg", outputFile);
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        } 
        message.getSender().tell(new CountMessage());
        return this;
    }
    
    public static Behavior<SplitAndSaveMessage> start() {
        return Behaviors.setup(ImageSplitSave::new);
    }
    
}

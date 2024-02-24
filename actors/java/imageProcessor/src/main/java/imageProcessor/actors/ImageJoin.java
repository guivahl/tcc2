package imageProcessor.actors;

import imageProcessor.messages.*;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.Instant;

import javax.imageio.ImageIO;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
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

            BufferedImage[][] images = new BufferedImage[this.splitHeight][this.splitWidth];
            
            for(int i = 0; i < this.splitHeight; i++){
                for(int j = 0; j < this.splitWidth; j++){
                    try {
                        String path =  String.format("tmp/rotated_%d_%d.png", i, j);

                        images[i][j] = ImageIO.read(new File(path));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
    
            BufferedImage combinedImage = new BufferedImage(message.getWidth(), message.getHeight(), BufferedImage.TYPE_INT_RGB);
    
            Graphics2D g2d = combinedImage.createGraphics();
    
            int xOffset = 0;
            int yOffset = 0;
    
            for(int i = (this.splitHeight - 1); i >= 0; i--){
                for(int j = (this.splitWidth - 1); j >= 0; j--){
                    g2d.drawImage(images[i][j], xOffset, yOffset, null);
                    xOffset += images[i][j].getWidth();
                    if (xOffset >= message.getWidth()) {
                        xOffset = 0;
                        yOffset += images[i][j].getHeight();
                    }
                }
            }
    
            g2d.dispose(); 
    
            String outputPath = "outputImages/final_" + System.currentTimeMillis() + ".png";

            try {
                ImageIO.write(combinedImage, "png", new File(outputPath));
            } catch (IOException e) {
                e.printStackTrace();
            }

        FinishSaveMessage responseMessage = new FinishSaveMessage(outputPath);

        message.getSender().tell(responseMessage);

        return this;
    }
    
    public static Behavior<JoinMessage> start() {
        return Behaviors.setup(ImageJoin::new);
    }
    
}

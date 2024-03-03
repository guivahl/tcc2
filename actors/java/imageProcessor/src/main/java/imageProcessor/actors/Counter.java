package imageProcessor.actors;

import imageProcessor.messages.*;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import java.util.TreeMap;

import javax.imageio.ImageIO;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

public class Counter extends AbstractBehavior<IMessage> {
    private int counter = 0;
    private int max;
    private int width;
    private int height;
    private int splitWidth;
    private int splitHeight;
    private Map<Integer, BufferedImage> images = new TreeMap<Integer, BufferedImage>();
    private ActorRef<IMessage> parentActor;

    public Counter(ActorContext<IMessage> context, int max, int width, int height, int splitWidth, int splitHeight, ActorRef<IMessage> parentActor) {
        super(context);
        this.max = max;
        this.width = width;
        this.height = height;
        this.splitWidth = splitWidth;
        this.splitHeight = splitHeight;
        this.parentActor = parentActor;
    }

    @Override
    public Receive<IMessage> createReceive() {
        return newReceiveBuilder()
            .onMessage(CountMessage.class, this::count)
            .build();
    }
    
    private Behavior<IMessage> count(CountMessage message) {     
        this.counter++;

        this.images.put(message.getOrder(), message.getRotatedImage());

        if (this.counter == this.max) {
            String joinActorName = "PIActor_Join";

            ActorRef<JoinMessage> imageJoinActor = getContext().spawn(ImageJoin.start(), joinActorName);

            JoinMessage joinMessage = new JoinMessage(this.parentActor, this.width, this.height, this.splitWidth, this.splitHeight, this.images);

            imageJoinActor.tell(joinMessage);
        }

        return this;
    }
    
    public static Behavior<IMessage> start(int max, int width, int height, int splitWidth, int splitHeight, ActorRef<IMessage> parentActor) {
        return Behaviors.setup(context -> new Counter(context, max, width, height, splitWidth, splitHeight, parentActor));
    }
    
}

package imageProcessor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;

import akka.actor.typed.ActorSystem;
import akka.actor.typed.javadsl.AbstractBehavior;
import imageProcessor.actors.ImageProcessor;
import imageProcessor.messages.IMessage;
import imageProcessor.messages.SplitAndInvertMessage;


public class App 
{
    public static Instant startTime;

    public static void main( String[] args ) throws IOException
    {
        startTime = Instant.now();

        String inputPath = "image.png";

        int splitWidth = 4;
        int splitHeight = 4;

        if (args.length == 2) {        
            splitWidth = Integer.parseInt(args[0]);
            splitHeight = Integer.parseInt(args[1]);
        }

        ActorSystem<IMessage> imageProcessorActor = ActorSystem.create(ImageProcessor.start(), "ImageProcessorActor");

        imageProcessorActor.tell(new SplitAndInvertMessage(inputPath, splitWidth, splitHeight));

    }
}

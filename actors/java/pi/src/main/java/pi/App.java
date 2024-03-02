package pi;

import java.time.Instant;

import akka.actor.typed.ActorSystem;
import akka.actor.typed.javadsl.AbstractBehavior;
import pi.actors.*;
import pi.messages.IMessage;
import pi.messages.PIMessage;

public class App 
{
    public static Instant startTime;
    
    public static void main(String[] args )
    {
        startTime = Instant.now();

        long totalIterations = 1_000L;
        long numActors = 1_000L;

        if (args.length == 2) {        
             totalIterations = Long.parseLong(args[0]);
             numActors = Long.parseLong(args[1]);
        }

        ActorSystem<IMessage> piActor = ActorSystem.create(PIEstimate.start(), "PIActor");

        piActor.tell(new PIMessage(totalIterations, numActors));
    }
}

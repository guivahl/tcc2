package pi;

import akka.actor.typed.ActorSystem;
import pi.actors.PI;
import pi.messages.IMessage;
import pi.messages.PIMessage;

public class App 
{
    public static void main( String[] args )
    {
        long totalIterations = 100_000_000L;
        long numActors = 10L;

        ActorSystem<IMessage> piActor = ActorSystem.create(PI.start(), "PIActor");

        piActor.tell(new PIMessage(totalIterations, numActors));
    }
}

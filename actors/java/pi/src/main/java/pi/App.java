package pi;

import akka.actor.typed.ActorSystem;
import pi.actors.PI;
import pi.messages.IMessage;
import pi.messages.PIMessage;

public class App 
{
    public static void main( String[] args )
    {
        int totalIterations = 1_000;
        int numActors = 10;

        ActorSystem<IMessage> piActor = ActorSystem.create(PI.start(), "PIActor");

        piActor.tell(new PIMessage(totalIterations, numActors));
    }
}

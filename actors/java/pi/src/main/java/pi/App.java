package pi;

import akka.actor.ActorRef;
import akka.actor.typed.ActorSystem;
import pi.messages.CalculationRequest;

public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        // ActorRef actor =  ActorSystem.create(PI.behavior(), "PIActor");
        ActorSystem<CalculationRequest> piActor = ActorSystem.create(PI.behavior(), "PIActor");
        
        piActor.tell(new CalculationRequest(100));
        piActor.tell(new CalculationRequest(200));
    }
}

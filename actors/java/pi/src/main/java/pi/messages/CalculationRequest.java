package pi.messages;

import akka.actor.ActorRef;
import pi.PI;

public class CalculationRequest {
    private int iterations;
    // private ActorRef<PI> sender;

    public CalculationRequest(int iterations) {
        this.iterations = iterations;
        // this.sender = sender;
    }

    // public getSender() {
    //     return this.sender;
    // }

    public int getIterations() {
        return this.iterations;
    }
}

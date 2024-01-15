package pi.messages;

public class PIMessage implements IMessage {
    private int iterations;
    private int numActors;

    public PIMessage(int iterations, int numActors) {
        this.iterations = iterations;
        this.numActors = numActors;
    }

    public int getIterations() {
        return this.iterations;
    }

    public int getNumActors() {
        return this.numActors;
    }

}

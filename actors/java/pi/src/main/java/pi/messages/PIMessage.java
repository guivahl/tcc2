package pi.messages;

public class PIMessage implements IMessage {
    private long iterations;
    private long numActors;

    public PIMessage(long totalIterations, long numActors) {
        this.iterations = totalIterations;
        this.numActors = numActors;
    }

    public long getIterations() {
        return this.iterations;
    }

    public long getNumActors() {
        return this.numActors;
    }

}

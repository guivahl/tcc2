package pi.messages;

public class PIMessage implements IMessage {
    private long totalPoints;
    private long numActors;

    public PIMessage(long totalPoints, long numActors) {
        this.totalPoints = totalPoints;
        this.numActors = numActors;
    }

    public long getTotalPoints() {
        return this.totalPoints;
    }

    public long getNumActors() {
        return this.numActors;
    }

}

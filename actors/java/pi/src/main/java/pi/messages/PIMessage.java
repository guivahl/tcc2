package pi.messages;

public class PIMessage implements IMessage {
    private long totalPoints;
    private long numActors;
    private long pointsPerActor;

    public PIMessage(long totalPoints, long numActors, long pointsPerActor) {
        this.totalPoints = totalPoints;
        this.numActors = numActors;
        this.pointsPerActor = pointsPerActor;
    }

    public long getTotalPoints() {
        return this.totalPoints;
    }

    public long getNumActors() {
        return this.numActors;
    }

    public long getPointsPerActor() {
        return this.pointsPerActor;
    }
}

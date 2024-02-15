package pi.messages;

public class CalculationResponseMessage implements IMessage {
    private long isInsideCounter;

    public CalculationResponseMessage(long isInsideCounter) {
        this.isInsideCounter = isInsideCounter;
    }

    public long getIsInsideCounter() {
        return this.isInsideCounter;
    }
}

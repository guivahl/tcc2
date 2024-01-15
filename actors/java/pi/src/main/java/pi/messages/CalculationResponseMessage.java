package pi.messages;

public class CalculationResponseMessage implements IMessage {
    private int isInsideCounter;

    public CalculationResponseMessage(int isInsideCounter) {
        this.isInsideCounter = isInsideCounter;
    }

    public int getIsInsideCounter() {
        return this.isInsideCounter;
    }
}

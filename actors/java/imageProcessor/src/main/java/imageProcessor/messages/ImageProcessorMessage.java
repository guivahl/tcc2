package imageProcessor.messages;

public class ImageProcessorMessage implements IMessage {
    private int splitWidth;
    private int splitHeight;

    public ImageProcessorMessage(int splitWidth, int splitHeight) {
        this.splitWidth = splitWidth;
        this.splitHeight = splitHeight;
    }

    public int getSplitWidth() {
        return this.splitWidth;
    }

    public int getSplitHeight() {
        return this.splitHeight;
    }
}

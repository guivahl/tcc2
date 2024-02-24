package imageProcessor.messages;

public class SplitAndInvertMessage implements IMessage {
    private String inputPath;
    private int splitWidth;
    private int splitHeight;

    public SplitAndInvertMessage(String inputPath, int splitWidth, int splitHeight) {
        this.inputPath = inputPath;
        this.splitWidth = splitWidth;
        this.splitHeight = splitHeight;
    }

    public String getInputPath() {
        return this.inputPath;
    }

    public int getSplitWidth() {
        return this.splitWidth;
    }

    public int getSplitHeight() {
        return this.splitHeight;
    }
}

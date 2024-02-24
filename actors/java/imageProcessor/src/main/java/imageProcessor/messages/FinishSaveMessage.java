package imageProcessor.messages;

import imageProcessor.*;

public class FinishSaveMessage implements IMessage {
    private String outputPath;

    public FinishSaveMessage(String outputPath) {
        this.outputPath = outputPath;
    }

    public String getOutputPath() {
        return this.outputPath;
    }
}

package chat.tidy.message;

import com.google.gson.annotations.Expose;

import java.util.Arrays;
import java.util.UUID;

public class ChatMessage {

    @Expose
    private UUID messageUID = null;
    @Expose
    private String message = null;
    @Expose
    private long timestamp;
    @Expose/*(serialize = false)*/
    private StatusCode[] statusCodes = new StatusCode[0];

    public ChatMessage() {
    }

    public ChatMessage(UUID messageUID, String message) {
        this(messageUID, message, System.currentTimeMillis());
    }

    public ChatMessage(UUID messageUID, String message, long timestamp) {
        this.messageUID = messageUID;
        this.message = message;
        this.timestamp = timestamp;
    }

    public UUID getMessageUID() {
        return messageUID;
    }

    public String getMessage() {
        return message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public StatusCode[] getStatusCodes() {
        return statusCodes;
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
                "messageUID=" + messageUID.toString() +
                ", message='" + message + "'" +
                ", timestamp=" + timestamp +
                ", statusCodes=" + Arrays.toString(statusCodes) +
                '}';
    }
}

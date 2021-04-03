package chat.tidy.message;

import com.google.gson.annotations.Expose;

import java.util.LinkedList;
import java.util.UUID;

public class Author {

    @Expose
    private UUID authorUID = null;
    @Expose
    private long creationDate = System.currentTimeMillis();
    @Expose
    private LinkedList<ChatMessage> chatMessages = new LinkedList<>();

    private Author() {
    }

    public Author(UUID authorUID) {
        this.authorUID = authorUID;
    }

    public UUID getAuthorUID() {
        return authorUID;
    }

    public long getCreationDate() {
        return creationDate;
    }

    public LinkedList<ChatMessage> getChatMessages() {
        return chatMessages;
    }

    @Override
    public String toString() {
        return "Author{" +
                "authorUID=" + authorUID.toString() +
                ", creationDate=" + creationDate +
                ", chatMessages=" + chatMessages.toString() +
                "}";
    }
}

package chat.tidy.event;

import chat.tidy.TidyChat;
import com.google.gson.annotations.Expose;

public abstract class Event {

    @Expose
    int id;

    public final int getId() {
        return id;
    }

    public final TidyChat getTidyChat() {
        return TidyChat.getInstance();
    }
}

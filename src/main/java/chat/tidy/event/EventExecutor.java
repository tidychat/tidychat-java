package chat.tidy.event;

import chat.tidy.listener.Listener;

public interface EventExecutor {

    void execute(Listener listener, Event event);
}

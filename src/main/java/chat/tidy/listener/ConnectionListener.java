package chat.tidy.listener;

import chat.tidy.TidyChat;
import chat.tidy.event.ConnectionStateChangedEvent;
import chat.tidy.event.EventHandler;
import chat.tidy.socket.ConnectionState;

public class ConnectionListener implements Listener {

    @EventHandler
    public void onConnectionStateChanged(ConnectionStateChangedEvent event) {
        TidyChat tidyChat = event.getTidyChat();
        ConnectionState connectionState = event.getConnectionState();
        if (tidyChat.getConnectionState() != connectionState) {
            switch (connectionState) {
                case OPEN:
                    tidyChat.getLoggerConsumer().accept("Connected!");
                    tidyChat.getReconnectRunnable().resetReconnectInterval();
                    break;
                case CLOSED:
                    tidyChat.getLoggerConsumer().accept(
                            tidyChat.getReconnectRunnable().isShutdownOrTerminated()
                                    ? "Disconnected! Will not attempt to reconnect due to terminated reconnect task!"
                                    : "Disconnected! Attempting to reconnect in " + (tidyChat.getReconnectRunnable().getReconnectInterval() / 1000) + "s..."
                    );
                    break;
            }

            tidyChat.setConnectionState(connectionState);
            tidyChat.setConnectionStateChangedTimestamp(System.currentTimeMillis());
        }
    }
}

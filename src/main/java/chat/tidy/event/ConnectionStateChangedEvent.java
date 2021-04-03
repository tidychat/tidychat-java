package chat.tidy.event;

import chat.tidy.socket.ConnectionState;

@EventDefinition(eventType = EventType.CONNECTION_STATE_CHANGE)
public class ConnectionStateChangedEvent extends Event {

    private final ConnectionState connectionState;

    public ConnectionStateChangedEvent(ConnectionState connectionState) {
        this.connectionState = connectionState;
    }

    public ConnectionState getConnectionState() {
        return connectionState;
    }

    @Override
    public String toString() {
        return "ConnectionStateChangedEvent{" +
                "id=" + id +
                ", connectionState=" + connectionState.name() +
                '}';
    }
}

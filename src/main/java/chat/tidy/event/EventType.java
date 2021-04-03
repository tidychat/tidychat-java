package chat.tidy.event;

public enum EventType {

    CONNECTION_STATE_CHANGE(ConnectionStateChangedEvent.class),
    PACKET_INBOUND(PacketInboundEvent.class),
    PACKET_OUTBOUND(PacketOutboundEvent.class);

    private final Class<? extends Event> constructor;

    EventType(Class<? extends Event> constructor) {
        this.constructor = constructor;
    }

    public Class<? extends Event> getConstructor() {
        return constructor;
    }
}

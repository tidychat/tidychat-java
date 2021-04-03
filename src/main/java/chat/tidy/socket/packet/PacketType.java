package chat.tidy.socket.packet;

public enum PacketType {

    CHECK_MESSAGE(0x00, CheckMessageInboundPacket.class, CheckMessageOutboundPacket.class),
    GET_USER_DATA(0x01, GetUserDataInboundPacket.class, GetUserDataOutboundPacket.class),
    DELETE_USER_DATA(0x02, DeleteUserDataInboundPacket.class, DeleteUserDataOutboundPacket.class);

    private final int id;
    private final Class<? extends InboundPacket> inboundClass;
    private final Class<? extends OutboundPacket> outboundClass;

    PacketType(int id, Class<? extends InboundPacket> inboundClass, Class<? extends OutboundPacket> outboundClass) {
        this.id = id;
        this.inboundClass = inboundClass;
        this.outboundClass = outboundClass;
    }

    public int getId() {
        return id;
    }

    public Class<? extends InboundPacket> getInboundClass() {
        return inboundClass;
    }

    public Class<? extends OutboundPacket> getOutboundClass() {
        return outboundClass;
    }

    public static PacketType fromId(int id) {
        for (PacketType packet : values()) {
            if (packet.getId() == id) {
                return packet;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "PacketType{" +
                "id=" + id +
                ", inboundClass=" + inboundClass.getSimpleName() +
                ", outboundClass=" + outboundClass.getSimpleName() +
                '}';
    }
}

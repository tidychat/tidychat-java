package chat.tidy.event;

import chat.tidy.socket.packet.InboundPacket;

@EventDefinition(eventType = EventType.PACKET_INBOUND)
public class PacketInboundEvent extends PacketEvent<InboundPacket> {

    public PacketInboundEvent(InboundPacket inboundPacket) {
        super(inboundPacket);
    }
}

package chat.tidy.event;

import chat.tidy.socket.packet.OutboundPacket;

@EventDefinition(eventType = EventType.PACKET_OUTBOUND)
public class PacketOutboundEvent extends PacketEvent<OutboundPacket> {

    public PacketOutboundEvent(OutboundPacket outboundPacket) {
        super(outboundPacket);
    }
}

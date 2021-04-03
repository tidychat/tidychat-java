package chat.tidy.event;

import chat.tidy.socket.packet.Packet;

public class PacketEvent<T extends Packet> extends Event {

    private final T packet;

    PacketEvent(T packet) {
        this.packet = packet;
    }

    public T getPacket() {
        return packet;
    }

    @Override
    public String toString() {
        return "PacketEvent{" +
                "id=" + id +
                ", packet=" + packet.toString() +
                '}';
    }
}

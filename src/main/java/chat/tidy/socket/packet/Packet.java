package chat.tidy.socket.packet;

import com.google.gson.annotations.Expose;

public abstract class Packet {

    @Expose
    private int id = -1;

    private final long timestamp = System.currentTimeMillis();

    Packet() {
        PacketDefinition packetDefinition = getClass().getAnnotation(PacketDefinition.class);
        if (packetDefinition != null) {
            this.id = packetDefinition.packetType().getId();
        }
    }

    public int getId() {
        return id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "Packet{" +
                "id=" + id +
                '}';
    }
}

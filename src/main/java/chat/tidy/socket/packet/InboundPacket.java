package chat.tidy.socket.packet;

import com.google.gson.annotations.Expose;

public abstract class InboundPacket extends Packet {

    @Expose
    private String server = "unknown";

    public String getServer() {
        return server;
    }
}

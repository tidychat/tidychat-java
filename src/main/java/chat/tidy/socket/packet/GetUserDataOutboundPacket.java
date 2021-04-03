package chat.tidy.socket.packet;

import com.google.gson.annotations.Expose;

import java.util.UUID;

@PacketDefinition(packetType = PacketType.GET_USER_DATA)
public class GetUserDataOutboundPacket extends OutboundPacket {

    @Expose
    private UUID authorUID = null;

    private GetUserDataOutboundPacket() {
    }

    public GetUserDataOutboundPacket(UUID authorUID) {
        super();
        this.authorUID = authorUID;
    }

    public UUID getAuthorUID() {
        return authorUID;
    }

    @Override
    public String toString() {
        return "GetUserDataOutboundPacket{" +
                "authorUID=" + authorUID.toString() +
                '}';
    }
}

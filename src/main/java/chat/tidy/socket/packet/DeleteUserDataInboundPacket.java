package chat.tidy.socket.packet;

import com.google.gson.annotations.Expose;

import java.util.UUID;

@PacketDefinition(packetType = PacketType.DELETE_USER_DATA)
public class DeleteUserDataInboundPacket extends InboundPacket {

    @Expose
    private UUID authorUID = null;

    private DeleteUserDataInboundPacket() {
    }

    public UUID getAuthorUID() {
        return authorUID;
    }

    @Override
    public String toString() {
        return "DeleteUserDataInboundPacket{" +
                "authorUID=" + authorUID.toString() +
                '}';
    }
}

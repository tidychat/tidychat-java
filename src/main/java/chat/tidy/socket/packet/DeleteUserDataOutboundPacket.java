package chat.tidy.socket.packet;

import com.google.gson.annotations.Expose;

import java.util.UUID;

@PacketDefinition(packetType = PacketType.DELETE_USER_DATA)
public class DeleteUserDataOutboundPacket extends OutboundPacket {

    @Expose
    private UUID authorUID = null;
    @Expose
    private boolean deleted = false;

    private DeleteUserDataOutboundPacket() {
    }

    public DeleteUserDataOutboundPacket(UUID authorUID, boolean deleted) {
        super();
        this.authorUID = authorUID;
        this.deleted = deleted;
    }

    public UUID getAuthorUID() {
        return authorUID;
    }

    public boolean isDeleted() {
        return deleted;
    }

    @Override
    public String toString() {
        return "DeleteUserDataOutboundPacket{" +
                "authorUID=" + authorUID.toString() +
                ", deleted=" + deleted +
                '}';
    }
}

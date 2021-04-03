package chat.tidy.socket.packet;

import chat.tidy.message.ChatMessage;
import com.google.gson.annotations.Expose;

import java.util.UUID;

@PacketDefinition(packetType = PacketType.CHECK_MESSAGE)
public class CheckMessageOutboundPacket extends OutboundPacket {

    @Expose
    private UUID authorUID;
    @Expose
    private ChatMessage chatMessage;

    private CheckMessageOutboundPacket() {
    }

    public CheckMessageOutboundPacket(UUID authorUID, ChatMessage chatMessage) {
        super();
        this.authorUID = authorUID;
        this.chatMessage = chatMessage;
    }

    public UUID getAuthorUID() {
        return authorUID;
    }

    public ChatMessage getChatMessage() {
        return chatMessage;
    }

    @Override
    public String toString() {
        return "CheckMessageOutboundPacket{" +
                "authorUID=" + authorUID.toString() +
                ", chatMessage=" + chatMessage.toString() +
                '}';
    }
}

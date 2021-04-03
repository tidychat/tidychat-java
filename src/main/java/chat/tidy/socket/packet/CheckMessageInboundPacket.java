package chat.tidy.socket.packet;

import chat.tidy.message.ChatMessage;
import com.google.gson.annotations.Expose;

@PacketDefinition(packetType = PacketType.CHECK_MESSAGE)
public class CheckMessageInboundPacket extends InboundPacket {

    @Expose
    private ChatMessage chatMessage = null;

    private CheckMessageInboundPacket() {
    }

    public ChatMessage getChatMessage() {
        return chatMessage;
    }

    @Override
    public String toString() {
        return "CheckMessageInboundPacket{" +
                "chatMessage=" + chatMessage.toString() +
                '}';
    }
}

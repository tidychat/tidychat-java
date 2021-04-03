package chat.tidy.socket.packet;

import chat.tidy.message.Author;
import com.google.gson.annotations.Expose;

@PacketDefinition(packetType = PacketType.GET_USER_DATA)
public class GetUserDataInboundPacket extends InboundPacket {

    @Expose
    private Author author = null;

    private GetUserDataInboundPacket() {
    }

    public Author getAuthor() {
        return author;
    }

    @Override
    public String toString() {
        return "GetUserDataInboundPacket{" +
                "authorUID=" + author.toString() +
                '}';
    }
}

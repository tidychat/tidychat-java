package chat.tidy.socket.handler;

import chat.tidy.TidyChat;
import chat.tidy.event.PacketInboundEvent;
import chat.tidy.json.GsonContainer;
import chat.tidy.socket.packet.InboundPacket;
import chat.tidy.socket.packet.PacketType;
import io.grpc.netty.shaded.io.netty.channel.ChannelHandlerContext;
import io.grpc.netty.shaded.io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

public class TextWebSocketFrameHandler extends InboundHandlerShard<TextWebSocketFrame> {

    @Override
    public void channelRead(ChannelHandlerContext ctx, TextWebSocketFrame frame) {
        String text = frame.text();
        if (text != null && !text.isEmpty()) {
            GsonContainer json = GsonContainer.serialize(text);
            if (json.has("id")) {
                PacketType packetType = PacketType.fromId(json.getInt("id"));
                if (packetType != null) {
                    InboundPacket inboundPacket = GsonContainer.deserialize(text, packetType.getInboundClass());
                    if (inboundPacket != null) {
                        TidyChat.getInstance().getEventManager().callEvent(new PacketInboundEvent(inboundPacket));
                    }
                }
            }
        }
    }
}
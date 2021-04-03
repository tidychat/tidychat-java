package chat.tidy.listener;

import chat.tidy.event.EventHandler;
import chat.tidy.event.PacketOutboundEvent;
import chat.tidy.json.GsonContainer;
import io.grpc.netty.shaded.io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

public class PacketListener implements Listener {

    @EventHandler
    public void onPacketOutbound(PacketOutboundEvent event) {
        event.getTidyChat().getSocketClient().send(new TextWebSocketFrame(GsonContainer.serialize(event.getPacket()).toString()));
    }
}

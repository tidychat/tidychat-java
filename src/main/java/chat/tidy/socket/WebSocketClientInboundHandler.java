package chat.tidy.socket;

import chat.tidy.TidyChat;
import chat.tidy.event.ConnectionStateChangedEvent;
import chat.tidy.socket.handler.InboundHandlerShard;
import chat.tidy.socket.handler.TextWebSocketFrameHandler;
import io.grpc.netty.shaded.io.netty.channel.ChannelHandler;
import io.grpc.netty.shaded.io.netty.channel.ChannelHandlerContext;
import io.grpc.netty.shaded.io.netty.channel.ChannelInboundHandlerAdapter;
import io.grpc.netty.shaded.io.netty.handler.codec.http.websocketx.WebSocketClientProtocolHandler;
import io.grpc.netty.shaded.io.netty.handler.codec.http.websocketx.WebSocketHandshakeException;
import io.grpc.netty.shaded.io.netty.util.ReferenceCountUtil;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@ChannelHandler.Sharable
class WebSocketClientInboundHandler extends ChannelInboundHandlerAdapter {

    private final Map<Class, Map<InboundHandlerShard, MethodHandle>> methods = new HashMap<>();

    WebSocketClientInboundHandler() {
        registerHandler(new TextWebSocketFrameHandler());
    }

    private void registerHandler(InboundHandlerShard inboundHandler) {
        Class genericType = inboundHandler.getGenericType();
        Map<InboundHandlerShard, MethodHandle> registry = methods.getOrDefault(genericType, new HashMap<>());
        for (Method method : inboundHandler.getClass().getMethods()) {
            if (method.getParameterTypes().length != 2 || !method.getParameterTypes()[1].equals(genericType)) {
                continue;
            }
            try {
                registry.put(inboundHandler, MethodHandles.lookup().unreflect(method));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        this.methods.put(genericType, registry);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) {
        TidyChat.getInstance().getSocketClient().close();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        TidyChat.getInstance().getSocketClient().close();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object object) {
        for (Map.Entry<Class, Map<InboundHandlerShard, MethodHandle>> classEntries : methods.entrySet()) {
            Class<?> genericClass = classEntries.getKey();
            if (genericClass.isAssignableFrom(object.getClass())) {
                for (Map.Entry<InboundHandlerShard, MethodHandle> handlerEntries : classEntries.getValue().entrySet()) {
                    try {
                        handlerEntries.getValue().invoke(handlerEntries.getKey(), ctx, object);
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        ReferenceCountUtil.release(object);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object event) {
        if (event instanceof WebSocketClientProtocolHandler.ClientHandshakeStateEvent) {
            WebSocketClientProtocolHandler.ClientHandshakeStateEvent clientHandshakeStateEvent = (WebSocketClientProtocolHandler.ClientHandshakeStateEvent) event;
            if (clientHandshakeStateEvent == WebSocketClientProtocolHandler.ClientHandshakeStateEvent.HANDSHAKE_COMPLETE) {
                TidyChat.getInstance().getEventManager().callEvent(new ConnectionStateChangedEvent(ConnectionState.OPEN));
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        if (cause instanceof WebSocketHandshakeException) {
            if (cause.getMessage().contains("Origin Down")) {
                TidyChat.getInstance().getLoggerConsumer().accept("The servers are temporarily offline for maintenance!");
            } else if (cause.getMessage().contains("Origin Error") || cause.getMessage().contains("OK")) {
                TidyChat.getInstance().getLoggerConsumer().accept("The servers had issues processing the login request!");
            } else if (cause.getMessage().contains("Conflict")) {
                TidyChat.getInstance().getLoggerConsumer().accept("A connection has already been established from another location! You can not log in multiple times with the same Bearer!");
            } else if (cause.getMessage().contains("Forbidden")) {
                TidyChat.getInstance().getLoggerConsumer().accept("A version mismatch occurred! Consider updating tidychat to keep using the service.");
            } else {
                TidyChat.getInstance().getLoggerConsumer().accept("Unknown error during handshake: " + cause.getMessage());
            }
            TidyChat.getInstance().getReconnectRunnable().incReconnectInterval();
            TidyChat.getInstance().getSocketClient().close();
            return;
        }
        cause.printStackTrace();
    }
}
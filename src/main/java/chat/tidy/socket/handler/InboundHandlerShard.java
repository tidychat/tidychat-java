package chat.tidy.socket.handler;

import io.grpc.netty.shaded.io.netty.channel.ChannelHandlerContext;

import java.lang.reflect.ParameterizedType;

public abstract class InboundHandlerShard<T> {

    public abstract void channelRead(ChannelHandlerContext ctx, T object);

    public Class getGenericType() {
        return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }
}

package chat.tidy.socket;

import chat.tidy.TidyChat;
import chat.tidy.event.ConnectionStateChangedEvent;
import io.grpc.netty.shaded.io.netty.bootstrap.Bootstrap;
import io.grpc.netty.shaded.io.netty.channel.*;
import io.grpc.netty.shaded.io.netty.channel.nio.NioEventLoopGroup;
import io.grpc.netty.shaded.io.netty.channel.socket.SocketChannel;
import io.grpc.netty.shaded.io.netty.channel.socket.nio.NioSocketChannel;
import io.grpc.netty.shaded.io.netty.handler.codec.http.DefaultHttpHeaders;
import io.grpc.netty.shaded.io.netty.handler.codec.http.HttpClientCodec;
import io.grpc.netty.shaded.io.netty.handler.codec.http.HttpHeaders;
import io.grpc.netty.shaded.io.netty.handler.codec.http.HttpObjectAggregator;
import io.grpc.netty.shaded.io.netty.handler.codec.http.websocketx.*;
import io.grpc.netty.shaded.io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketClientCompressionHandler;
import io.grpc.netty.shaded.io.netty.handler.ssl.SslContext;
import io.grpc.netty.shaded.io.netty.handler.ssl.SslContextBuilder;
import io.grpc.netty.shaded.io.netty.util.concurrent.FutureListener;

import javax.net.ssl.SSLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WebSocketClient {

    private static final WebSocketClientInboundHandler INBOUND_HANDLER = new WebSocketClientInboundHandler();

    private static URI REMOTE_URI = null;
    private static SslContext SSL_CONTEXT = null;
    private static HttpHeaders HEADERS = new DefaultHttpHeaders();

    private Bootstrap bootstrap = null;
    private Channel channel = null;
    private ExecutorService executor = null;

    public WebSocketClient(TidyChat tidyChat) {
        try {
            REMOTE_URI = new URI("wss://tidy.chat:443");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        if (REMOTE_URI != null && REMOTE_URI.getPort() == 443) {
            try {
                SSL_CONTEXT = SslContextBuilder.forClient().build();
            } catch (SSLException e) {
                e.printStackTrace();
            }
        }

        HEADERS.set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.UPGRADE);
        HEADERS.set(HttpHeaders.Names.UPGRADE, HttpHeaders.Values.WEBSOCKET);
        HEADERS.set("Version", tidyChat.getProperties().getProperty("version", "unknown"));
        if (tidyChat.getBearer() != null) {
            HEADERS.set("Bearer", tidyChat.getBearer());
        }
    }

    public void connect() {
        TidyChat.getInstance().getEventManager().callEvent(new ConnectionStateChangedEvent(ConnectionState.CONNECTING));

        if (REMOTE_URI == null) {
            return;
        }

        if (bootstrap == null) {
            bootstrap = new Bootstrap();
            bootstrap.channel(NioSocketChannel.class)
                    .group(new NioEventLoopGroup())
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            if (SSL_CONTEXT != null) {
                                pipeline.addLast("ssl", SSL_CONTEXT.newHandler(socketChannel.alloc(), REMOTE_URI.getHost(), REMOTE_URI.getPort()));
                            }
                            pipeline.addLast("codec", new HttpClientCodec());
                            pipeline.addLast("aggregator", new HttpObjectAggregator(65536));
                            pipeline.addLast("compression", WebSocketClientCompressionHandler.INSTANCE);
                            pipeline.addLast("protocolhandler", new WebSocketClientProtocolHandler(WebSocketClientHandshakerFactory.newHandshaker(REMOTE_URI, WebSocketVersion.V13, null, true, HEADERS)));
                            pipeline.addLast("inboundhandler", INBOUND_HANDLER);
                        }
                    });
        }

        this.close();
        this.executor = Executors.newSingleThreadExecutor();
        this.executor.submit(() -> {
            try {
                ChannelFuture channelFuture = bootstrap.connect(REMOTE_URI.getHost(), REMOTE_URI.getPort());
                channel = channelFuture.addListener((FutureListener<Void>) voidFuture -> {
                    if (channelFuture.isDone() && !channelFuture.isSuccess()) this.close();
                }).sync().channel();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void close() {
        try {
            boolean isChannelInitiated = channel != null;
            boolean isExecutorShutdown = executor == null || executor.isShutdown();
            if (isChannelInitiated || !isExecutorShutdown) {
                TidyChat.getInstance().getEventManager().callEvent(new ConnectionStateChangedEvent(ConnectionState.CLOSING));
                if (isChannelInitiated) {
                    channel.writeAndFlush(new CloseWebSocketFrame());
                    channel.close();
                    channel = null;
                }
                if (!isExecutorShutdown) executor.shutdown();
                TidyChat.getInstance().getEventManager().callEvent(new ConnectionStateChangedEvent(ConnectionState.CLOSED));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void send(WebSocketFrame frame) {
        if (channel != null) {
            channel.writeAndFlush(frame);
        }
    }
}
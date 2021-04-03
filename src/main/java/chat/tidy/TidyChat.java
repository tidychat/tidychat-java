package chat.tidy;

import chat.tidy.event.EventManager;
import chat.tidy.runnable.ReconnectRunnable;
import chat.tidy.socket.ConnectionState;
import chat.tidy.socket.WebSocketClient;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;
import java.util.function.Consumer;

public class TidyChat {

    private static TidyChat instance;

    private Consumer<String> loggerConsumer = s -> System.out.println("[" + new SimpleDateFormat("HH:mm:ss").format(new Date()) + "] [tidychat] " + s);

    private final Properties properties = new Properties();
    private final EventManager eventManager;
    private final WebSocketClient socketClient;
    private final ReconnectRunnable reconnectRunnable;

    private String bearer = null;

    private ConnectionState connectionState = ConnectionState.VIRGIN;
    private long connectionStateChangedTimestamp = System.currentTimeMillis();

    private TidyChat() {
        try {
            this.properties.load(getClass().getClassLoader().getResourceAsStream("tidychat.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.eventManager = new EventManager();
        this.socketClient = new WebSocketClient(this);
        this.reconnectRunnable = new ReconnectRunnable();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            reconnectRunnable.shutdown();
            socketClient.close();
        }));
    }

    public static TidyChat getInstance() {
        if (instance == null) {
            instance = new TidyChat();
        }
        return instance;
    }

    public Consumer<String> getLoggerConsumer() {
        return loggerConsumer;
    }

    public void setLoggerConsumer(Consumer<String> loggerConsumer) {
        this.loggerConsumer = loggerConsumer;
    }

    public Properties getProperties() {
        return properties;
    }

    public EventManager getEventManager() {
        return eventManager;
    }

    public WebSocketClient getSocketClient() {
        return socketClient;
    }

    public ReconnectRunnable getReconnectRunnable() {
        return reconnectRunnable;
    }

    public String getBearer() {
        return bearer;
    }

    public void setBearer(String bearer) {
        this.bearer = bearer;
    }

    public ConnectionState getConnectionState() {
        return connectionState;
    }

    public void setConnectionState(ConnectionState connectionState) {
        this.connectionState = connectionState;
    }

    public long getConnectionStateChangedTimestamp() {
        return connectionStateChangedTimestamp;
    }

    public void setConnectionStateChangedTimestamp(long connectionStateChangedTimestamp) {
        this.connectionStateChangedTimestamp = connectionStateChangedTimestamp;
    }
}

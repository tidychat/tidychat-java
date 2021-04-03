package chat.tidy.runnable;

import chat.tidy.TidyChat;
import chat.tidy.socket.ConnectionState;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ReconnectRunnable implements Runnable {

    private static final ScheduledExecutorService SERVICE = Executors.newSingleThreadScheduledExecutor();

    private static final int RECONNECT_INTERVAL_MS = 5 * 1000;
    private static final int MAX_RECONNECT_INTERVAL_MS = 160 * 1000;

    private final ScheduledFuture scheduledFuture = SERVICE.scheduleAtFixedRate(this, 1000, 1000, TimeUnit.MILLISECONDS);
    private int reconnectInterval = RECONNECT_INTERVAL_MS;

    @Override
    public void run() {
        try {
            ConnectionState connectionState = TidyChat.getInstance().getConnectionState();
            if (connectionState == ConnectionState.CLOSED) {
                long connectionStateChangedTimestamp = TidyChat.getInstance().getConnectionStateChangedTimestamp();
                if (System.currentTimeMillis() - connectionStateChangedTimestamp >= getReconnectInterval()) {
                    TidyChat.getInstance().getSocketClient().connect();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getReconnectInterval() {
        return reconnectInterval;
    }

    public void incReconnectInterval() {
        if (reconnectInterval >= MAX_RECONNECT_INTERVAL_MS) return;
        reconnectInterval *= 2;
    }

    public void resetReconnectInterval() {
        reconnectInterval = ReconnectRunnable.RECONNECT_INTERVAL_MS;
    }

    public void shutdown() {
        if (!isShutdownOrTerminated()) {
            SERVICE.shutdown();
        }
    }

    public boolean isShutdownOrTerminated() {
        return SERVICE.isShutdown() || SERVICE.isTerminated() || scheduledFuture.isDone() || scheduledFuture.isCancelled();
    }
}
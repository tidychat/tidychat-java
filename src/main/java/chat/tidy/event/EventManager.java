package chat.tidy.event;

import chat.tidy.listener.ConnectionListener;
import chat.tidy.listener.Listener;
import chat.tidy.listener.PacketListener;
import com.google.common.base.Preconditions;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class EventManager {

    private final Map<EventType, Map<Listener, Set<MethodHandle>>> methods = new HashMap<>();

    public EventManager() {
        registerListener(new PacketListener());
        registerListener(new ConnectionListener());
    }

    public void registerListener(Listener listener) {
        for (EventType eventType : EventType.values()) {
            Map<Listener, Set<MethodHandle>> registry = methods.getOrDefault(eventType, new HashMap<>());
            Set<MethodHandle> methods = registry.getOrDefault(listener, new HashSet<>());
            for (Method method : listener.getClass().getMethods()) {
                EventHandler eventHandler = method.getAnnotation(EventHandler.class);
                if (eventHandler == null || method.getParameterTypes().length != 1 || !method.getParameterTypes()[0].equals(eventType.getConstructor())) {
                    continue;
                }
                try {
                    methods.add(MethodHandles.lookup().unreflect(method));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            registry.put(listener, methods);
            this.methods.put(eventType, registry);
        }
    }

    public void callEvent(Event event) {
        Preconditions.checkNotNull(event);
        EventDefinition eventDefinition = event.getClass().getAnnotation(EventDefinition.class);
        if (eventDefinition != null && eventDefinition.isEnabled() && methods.containsKey(eventDefinition.eventType())) {
            for (Map.Entry<Listener, Set<MethodHandle>> entry : methods.get(eventDefinition.eventType()).entrySet()) {
                Listener listener = entry.getKey();
                Set<MethodHandle> methods = entry.getValue();
                for (MethodHandle method : methods) {
                    try {
                        method.invoke(listener, event);
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}

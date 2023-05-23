package com.github.allinkdev.kissevents;

import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

/**
 * The {@link EventSystem} class.
 */
public class EventSystem {
    /**
     * An unsynchronised {@link EventSystem} with the default settings.
     */
    public static final EventSystem UNSYNCHRONISED = new EventSystem(false);
    /**
     * A synchronised {@link EventSystem} with the default settings.
     */
    public static final EventSystem SYNCHRONISED = new EventSystem(true);
    private final Executor deferredExecutor = Executors.newCachedThreadPool();
    private final Map<Class<?>, Collection<Listener<Object>>> listenerMap;
    private final Supplier<Collection<Listener<Object>>> collectionSupplier;
    private final boolean synchronised;

    /**
     * Initialise an {@link EventSystem} with a custom Map and Collection type supplier.
     *
     * @param listenerMapSupplier A supplier of new Maps
     * @param collectionSupplier  A supplier of new Collections
     * @param synchronised        Are the Maps and Collections provided by the suppliers synchronised?
     */
    public EventSystem(final Supplier<Map<Class<?>, Collection<Listener<Object>>>> listenerMapSupplier,
                       final Supplier<Collection<Listener<Object>>> collectionSupplier,
                       final boolean synchronised) {
        this.listenerMap = listenerMapSupplier.get();
        this.collectionSupplier = collectionSupplier;
        this.synchronised = synchronised;
    }

    /**
     * Constructs a synchronised {@link EventSystem} with the default settings.
     */
    public EventSystem() {
        this(true);
    }

    /**
     * Construct a {@link EventSystem} with the default settings.
     *
     * @param synchronised whether to pass the default Map/Set through their equivalent Collections synchronized method
     */
    public EventSystem(final boolean synchronised) {
        this(
                synchronised ? () -> Collections.synchronizedMap(new HashMap<>()) : HashMap::new,
                synchronised ? () -> Collections.synchronizedSet(new HashSet<>()) : HashSet::new,
                synchronised
        );
    }

    private Collection<Listener<Object>> getListenerCollection(final Listener<?> listener) {
        final Class<?> key = listener.getType();
        Collection<Listener<Object>> value = this.listenerMap.get(key);

        if (value != null) {
            return value;
        }

        value = this.collectionSupplier.get();
        this.listenerMap.put(key, value);

        return value;
    }

    /**
     * Register a single listener.
     *
     * @param listener The listener to register
     */
    @SuppressWarnings("unchecked")
    public void registerListener(final Listener<?> listener) {
        getListenerCollection(listener).add((Listener<Object>) listener);
    }

    /**
     * Register multiple listeners.
     *
     * @param listeners The listeners to register
     */
    public void registerListeners(final Listener<?>... listeners) {
        Arrays.stream(listeners).forEach(this::registerListener);
    }

    /**
     * Unregister a single listener.
     *
     * @param listener The listener to unregister
     */
    public void unregisterListener(final Listener<?> listener) {
        getListenerCollection(listener).remove(listener);
    }

    /**
     * Unregister multiple listeners.
     *
     * @param listeners The listeners to unregister
     */
    public void unregisterListeners(final Listener<?>... listeners) {
        Arrays.stream(listeners).forEach(this::unregisterListener);
    }

    /**
     * Unregister a collection of listeners
     *
     * @param listenersCollection The collection of listeners to unregister
     */
    public void unregisterListeners(final Collection<Listener<?>> listenersCollection) {
        listenersCollection.forEach(this::unregisterListener);
    }

    /**
     * Unregister every listener.
     */
    public void unregisterAll() {
        this.listenerMap.clear();
    }


    /**
     * Post an event to the event system. If this event does not have any listeners, it will fail silently.
     *
     * @param event The event to post
     */
    public void post(final Object event) {
        final Class<?> eventClass = event.getClass();
        final Collection<Listener<Object>> listenerCollection = this.listenerMap.get(eventClass);

        if (listenerCollection == null || listenerCollection.isEmpty()) {
            return;
        }

        for (Listener<Object> listener : listenerCollection) {
            listener.accept(event);
        }
    }

    /**
     * Post an event to the event system, but defer the execution to another thread. This is a non-blocking alternative to {@link EventSystem#post(Object)}
     * <br>
     * <br>
     * <strong>This will act the same as {@link EventSystem#post(Object)} if invoked on a {@link EventSystem} that isn't marked as synchronised in the constructor.</strong>
     *
     * @param event The event to post
     */
    public void deferPost(final Object event) {
        if (this.synchronised) {
            this.post(event);
            return;
        }

        deferredExecutor.execute(() -> this.post(event));
    }
}

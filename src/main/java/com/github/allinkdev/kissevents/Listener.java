package com.github.allinkdev.kissevents;

import java.util.function.Consumer;

/**
 * The listener interface.
 *
 * @param <E> The event you want to listen to
 */
public interface Listener<E> extends Consumer<E> {
    /**
     * @return the class of the event you're listening to
     */
    Class<? extends E> getType();
}
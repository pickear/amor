package com.amor.common.listener;

import com.amor.common.listener.event.AbstractEvent;
import com.google.common.eventbus.Subscribe;

/**
 * Created by Dylan on 2018/1/7.
 */
public abstract class EventListener<T extends AbstractEvent> {

    @Subscribe
    public void receiveEvent(T event){
        try {
            handleEvent(event);
        }catch (ClassCastException e){
            // ignore ClassCastException
        }
    }

    /**
     *
     * @param event
     */
    protected abstract void handleEvent(T event);

}

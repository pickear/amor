package com.amor.core.listener.event;

import com.amor.core.listener.EventBus;
import com.amor.core.listener.EventListener;

/**
 * Created by Dylan on 2018/1/7.
 */
public final class EventPublisher {

    public final static EventBus EVENT_BUS = new EventBus();

    public static void register(EventListener listener){
        EVENT_BUS.addObserver(listener);
    }

    public static void unRegister(EventListener listener){
        EVENT_BUS.deleteObserver(listener);
    }

    public static void postEvent(AbstractEvent event){
        EVENT_BUS.setChanged();
        EVENT_BUS.notifyObservers(event);
    }

    private EventPublisher(){}
}

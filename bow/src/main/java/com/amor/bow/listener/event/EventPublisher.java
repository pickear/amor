package com.amor.bow.listener.event;

import com.amor.bow.listener.EventListener;
import com.google.common.eventbus.EventBus;

/**
 * Created by Dylan on 2018/1/7.
 */
public final class EventPublisher {

    public final static EventBus EVENT_BUS = new EventBus();

    public static void register(EventListener listener){
        EVENT_BUS.register(listener);
    }

    public static void unRegister(EventListener listener){
        EVENT_BUS.unregister(listener);
    }

    public static void postEvent(AbstractEvent event){
        EVENT_BUS.post(event);
    }

    private EventPublisher(){}
}

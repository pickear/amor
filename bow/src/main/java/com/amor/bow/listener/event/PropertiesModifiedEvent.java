package com.amor.bow.listener.event;

import org.springframework.context.ApplicationEvent;

/**
 * Created by dell on 2017/10/25.
 */
public class PropertiesModifiedEvent extends ApplicationEvent{
    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public PropertiesModifiedEvent(Object source) {
        super(source);
    }
}

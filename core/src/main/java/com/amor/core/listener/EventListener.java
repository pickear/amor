package com.amor.core.listener;

import com.amor.core.listener.event.AbstractEvent;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by Dylan on 2018/1/7.
 */
public abstract class EventListener<T extends AbstractEvent>  implements Observer {

    @Override
    public void update(Observable o, Object event) {

        try {
            handleEvent((T)event);
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

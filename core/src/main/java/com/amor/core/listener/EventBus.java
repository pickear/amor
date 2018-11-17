package com.amor.core.listener;

import java.util.Observable;

public class EventBus extends Observable {

    @Override
    public synchronized void setChanged() {
        super.setChanged();
    }

    @Override
    public synchronized void clearChanged() {
        super.clearChanged();
    }
}

package com.amor.bow.controller;

import com.amor.core.listener.event.EventPublisher;
import com.amor.core.listener.event.PropertiesModifiedEvent;

/**
 * Created by dylan on 2017/10/19.
 */
public class AdminController {

    public String reloadProperties(){

        EventPublisher.postEvent(new PropertiesModifiedEvent());
        return "success";
    }
}

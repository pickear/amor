package com.amor.bow.controller;

import com.amor.bow.listener.event.EventPublisher;
import com.amor.bow.listener.event.PropertiesModifiedEvent;
/**
 * Created by dylan on 2017/10/19.
 */
public class AdminController {

    public String reloadProperties(){

        EventPublisher.postEvent(new PropertiesModifiedEvent());
        return "success";
    }
}

package com.amor.bow.controller;

import com.amor.bow.listener.event.EventPublisher;
import com.amor.bow.listener.event.PropertiesModifiedEvent;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by dylan on 2017/10/19.
 */
@RestController
public class BowController {

    @RequestMapping("/")
    @ResponseBody
    public String http(){
        return "http";
    }

    @RequestMapping("/reloadProperties")
    @ResponseBody
    public String reloadProperties(){

        EventPublisher.publishEvent(new PropertiesModifiedEvent(this));
        return "success";
    }
}

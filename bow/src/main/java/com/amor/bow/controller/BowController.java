package com.amor.bow.controller;

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
}

package com.amor.bow.repository.impl;

import com.amor.bow.config.BowProperties;
import com.amor.common.model.User;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by dylan on 2017/10/26.
 */
public class UserLoader {
    private final static Logger logger = LoggerFactory.getLogger(UserLoader.class);
    private List<User> users = Lists.newArrayList();

    public UserLoader() {
        load();
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public UserLoader load(){
        if(this.users.isEmpty()){
            reload();
        }
        return this;
    }

    public UserLoader reload(){
        try {
            BowProperties properties = BowProperties.reInstance();
            setUsers(properties.getUsers());
        } catch (Exception e) {
            logger.error("can not find the application.yml from classpath,please create it.",e);
        }
        return this;
    }
}

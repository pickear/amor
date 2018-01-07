package com.amor.bow.repository.impl;

import ch.qos.logback.core.util.FileUtil;
import com.amor.common.helper.YamlHelper;
import com.amor.common.model.User;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by dylan on 2017/10/26.
 */
public class UserLoader {
    private final static Logger logger = LoggerFactory.getLogger(UserLoader.class);
    private List<User> users = Lists.newArrayList();

    public UserLoader() {
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    @PostConstruct
    public UserLoader load(){
        if(this.users.isEmpty()){
            reload();
        }
        return this;
    }

    public UserLoader reload(){
        try {
            UserLoader loader = YamlHelper.load(new FileInputStream("application-users.yml"),UserLoader.class);
            setUsers(loader.getUsers());
        } catch (IOException e) {
            logger.error("can not find the application-users.yml from classpath,please create it.");
        }
        return this;
    }
}

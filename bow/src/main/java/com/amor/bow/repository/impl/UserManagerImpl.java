package com.amor.bow.repository.impl;

import com.amor.bow.config.BowProxyProperties;
import com.amor.bow.repository.UserManager;
import com.amor.common.model.Device;
import com.amor.common.model.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by dell on 2017/10/12.
 */
@Repository
public class UserManagerImpl implements UserManager {

    @Autowired
    private BowProxyProperties properties;

    @Override
    public User getByName(String username) {

        return properties.getUsers()
                         .stream()
                         .filter(user -> StringUtils.equals(user.getUsername(),username))
                         .findFirst()
                         .orElse(null);
    }

    @Override
    public User getByDevicePort(int port) {

        return list().stream()
                     .filter(user -> {
                         Device _device = user.getDevices()
                                 .stream()
                                 .filter(device -> device.getRemotePort() == port)
                                 .findFirst()
                                 .orElse(null);
                         return null != _device;
                     })
                     .findFirst()
                     .orElse(null);
    }

    @Override
    public List<User> list() {
        return properties.getUsers();
    }
}

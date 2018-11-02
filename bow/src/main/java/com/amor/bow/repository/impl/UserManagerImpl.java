package com.amor.bow.repository.impl;

import com.amor.bow.repository.UserManager;
import com.amor.core.model.Device;
import com.amor.core.model.User;
import org.apache.commons.lang3.StringUtils;
import java.util.List;

/**
 * Created by dell on 2017/10/12.
 */
public class UserManagerImpl implements UserManager {

    private UserLoader userLoader = new UserLoader();

    public UserManagerImpl(){
    }

    @Override
    public User getByName(String username) {

        return list().stream()
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
        return userLoader.getUsers();
    }
}

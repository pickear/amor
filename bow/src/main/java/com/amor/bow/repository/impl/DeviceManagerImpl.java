package com.amor.bow.repository.impl;

import com.amor.bow.repository.DeviceManager;
import com.amor.bow.repository.UserManager;
import com.amor.common.model.Device;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by dell on 2017/10/12.
 */
@Repository
public class DeviceManagerImpl implements DeviceManager {

    @Autowired
    private UserManager userManager;

    @Override
    public List<Device> getByUsername(String username) {

        return userManager.getByName(username)
                          .getDevices();
    }

    @Override
    public Device getByPort(int port) {

        return list().stream()
                     .filter(device -> device.getRemotePort() == port)
                     .findFirst()
                     .orElse(null);
    }

    @Override
    public List<Device> list() {
        return userManager.list()
                          .stream()
                          .flatMap(user -> user.getDevices().stream())
                          .collect(Collectors.toList());
    }

    @Override
    public Device get(long id) {
        return list().stream()
                     .filter(device -> device.getId() == id)
                     .findFirst()
                     .orElse(null);
    }
}

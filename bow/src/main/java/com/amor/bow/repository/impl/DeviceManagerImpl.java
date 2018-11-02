package com.amor.bow.repository.impl;

import com.amor.bow.repository.DeviceManager;
import com.amor.bow.repository.UserManager;
import com.amor.core.model.Device;
import org.apache.commons.lang3.StringUtils;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by dell on 2017/10/12.
 */
public class DeviceManagerImpl implements DeviceManager {

    private UserManager userManager = new UserManagerImpl();

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
    public Device getBySubDomain(String subDomain) {
        return list().stream()
                     .filter(device -> StringUtils.equals(device.getSubDomain(),subDomain))
                     .findFirst()
                     .orElse(null);
    }

    @Override
    public List<Device> list() {
        List<Device> devices =  userManager.list()
                                           .stream()
                                           .flatMap(user -> user.getDevices().stream())
                                           .collect(Collectors.toList());
        return devices;
    }

    @Override
    public Device get(long id) {
        return list().stream()
                     .filter(device -> device.getId() == id)
                     .findFirst()
                     .orElse(null);
    }
}

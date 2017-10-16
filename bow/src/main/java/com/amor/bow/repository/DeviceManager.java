package com.amor.bow.repository;

import com.amor.common.model.Device;

import java.util.List;

/**
 * Created by dell on 2017/10/12.
 */
public interface DeviceManager {

    /**
     *
     * @param username
     * @return
     */
    List<Device> getByUsername(String username);

    /**
     *
     * @param port
     * @return
     */
    Device getByPort(int port);

    /**
     *
     * @return
     */
    List<Device> list();

    /**
     *
     * @param id
     * @return
     */
    Device get(long id);
}

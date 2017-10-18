package com.amor.bow.repository;

import com.amor.common.model.Device;

import java.util.List;

/**
 * Created by dell on 2017/10/12.
 */
public interface DeviceManager {

    /**
     *获取用户下所有的设备
     * @param username
     * @return
     */
    List<Device> getByUsername(String username);

    /**
     *通过端口获取设备
     * @param port
     * @return
     */
    Device getByPort(int port);

    /**
     *通过二级域名获取设备
     * @param subDomain
     * @return
     */
    Device getBySubDomain(String subDomain);

    /**
     *获取所有设备
     * @return
     */
    List<Device> list();

    /**
     *通过设备id获取设备
     * @param id
     * @return
     */
    Device get(long id);
}

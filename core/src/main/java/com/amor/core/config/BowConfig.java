package com.amor.core.config;

import com.amor.core.model.Device;
import com.amor.core.model.User;

import java.util.List;

/**
 * @author dylan
 * @date 2018/11/9
 */
public interface BowConfig {

    /**
     *
     * @return
     */
    String getLocalIp();

    /**
     *
     * @return
     */
    int getTcpPort();

    /**
     *
     * @return
     */
    int getHttpPort();

    /**
     *
     * @return
     */
    int getHttpsPort();

    /**
     *
     * @return
     */
    int getStartPort();

    /**
     *
     * @return
     */
    int getEndPort();

    /**
     *
     * @return
     */
    String getDomain();

    /**
     *
     * @return
     */
    List<User> getUsers();

    /**
     *
     * @param port
     * @return
     */
    Device getDeviceByPort(int port);

    /**
     *
     * @return
     */
    List<Device> getDevices();

    /**
     *
     * @param username
     * @return
     */
    User getUserByName(String username);

    /**
     *
     * @param subDomain
     * @return
     */
    Device getDeviceBySubDomain(String subDomain);


    /**
     *
     * @param username
     * @return
     */
    List<Device> getDeviceByUsername(String username);

    /**
     *
     * @param id
     * @return
     */
    Device getDeviceById(long id);
}

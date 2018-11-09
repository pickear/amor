package com.amor.core.config;

import com.amor.core.model.Device;

import java.util.List;

/**
 * @author dylan
 * @date 2018/11/9
 */
public interface ArrowConfig {

    /**
     *
     * @return
     */
    String getBowIp();

    /**
     *
     * @return
     */
    int getBowPort();

    /**
     *
     * @return
     */
    String getUsername();

    /**
     *
     * @return
     */
    String getPassword();

    /**
     *
     * @return
     */
    List<Device> getDevices();

}

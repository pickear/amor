package com.amor.core.config;

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
}

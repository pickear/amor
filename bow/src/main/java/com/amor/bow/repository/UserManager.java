package com.amor.bow.repository;

import com.amor.core.model.User;

import java.util.List;

/**
 * Created by dell on 2017/10/12.
 */
public interface UserManager {

    /**
     *
     * @param username
     * @return
     */
    User getByName(String username);

    /**
     *
     * @param port
     * @return
     */
    User getByDevicePort(int port);

    /**
     *
     * @return
     */
    List<User> list();
}

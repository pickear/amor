package com.amor.common.model;

import org.msgpack.annotation.Message;
import org.msgpack.annotation.MessagePackMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dylan on 2017/9/30.
 */
@MessagePackMessage
public class User extends Model{

    private String username;

    private String password;

    List<Device> devices = new ArrayList<>();

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Device> getDevices() {
        return devices;
    }

    public void setDevices(List<Device> devices) {
        this.devices = devices;
    }
}

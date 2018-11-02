package com.amor.core.model;

import org.msgpack.annotation.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dylan on 2017/10/8.
 */
@Message
public class Arrow extends Model {
    private String username;
    private String password;
    private List<Device> devices = new ArrayList<>();

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

    public void addDevice(Device device){
        this.devices.add(device);
    }
}

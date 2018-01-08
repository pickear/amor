package com.amor.arrow.config;

import com.amor.common.model.Device;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell on 2017/10/9.
 */
public class ArrowProperties {

    private String bowIp = "127.0.0.1";
    private int bowPort = 9998;
    private String username = "aaa";
    private String password = "bbb";
    private List<Device> devices = new ArrayList<Device>(){{
        add(new Device(1l));
        add(new Device(2l));
    }};

    public String getBowIp() {
        return bowIp;
    }

    public void setBowIp(String bowIp) {
        this.bowIp = bowIp;
    }

    public int getBowPort() {
        return bowPort;
    }

    public void setBowPort(int bowPort) {
        this.bowPort = bowPort;
    }

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

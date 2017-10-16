package com.amor.arrow.config;

import com.amor.common.model.Device;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by dell on 2017/10/9.
 */
@Component
@ConfigurationProperties(
        prefix = "arrow"
)
public class ArrowProperties {

    private String bowIp;
    private int bowPort;
    private String username;
    private String password;
    private List<Device> devices;

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

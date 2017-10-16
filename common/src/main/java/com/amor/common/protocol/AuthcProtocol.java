package com.amor.common.protocol;

import com.amor.common.model.Device;
import org.msgpack.annotation.Message;

import java.util.List;

/**
 * Created by dell on 2017/10/10.
 */
@Message
public class AuthcProtocol extends AbstractProtocol{

    private String username;
    private String password;
    private List<Device> devices;

    public AuthcProtocol() {
        super(AuthcProtocol.class.getName());
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

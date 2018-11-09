package com.amor.plugin.arrow.config;

import com.amor.core.config.ArrowConfig;
import com.amor.core.helper.ClassPathResourceHelper;
import com.amor.core.helper.YamlHelper;
import com.amor.core.model.Device;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell on 2017/10/9.
 */
public class ArrowConfiguration implements ArrowConfig {

    private static ArrowConfiguration configuration = null;
    private String bowIp = "127.0.0.1";
    private int bowPort = 9998;
    private String username = "aaa";
    private String password = "bbb";
    private List<Device> devices = new ArrayList<Device>(){{
        add(new Device(1l));
        add(new Device(2l));
    }};


    /**
     *
     * @return
     */
    public static synchronized ArrowConfiguration instance(){
        if(null == configuration){
            reInstance();
        }
        return configuration;
    }

    /**
     *
     * @return
     */
    public static synchronized ArrowConfiguration reInstance(){
        ArrowConfiguration _properties = YamlHelper.load(
                ClassPathResourceHelper.getInputStream("application.yml"),ArrowConfiguration.class
        );
        configuration = _properties;
        return configuration;
    }
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

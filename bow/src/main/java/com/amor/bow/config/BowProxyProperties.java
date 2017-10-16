package com.amor.bow.config;

import com.amor.common.model.User;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author dylan
 * @time 2017/6/15
 */
@Component
@ConfigurationProperties(
        prefix = "bow"
)
public class BowProxyProperties {

    private String localIp;
    private int tcpPort;  //tcp监听的端口
    private int httpPort;  //http监听的端口
    private int startPort; //分配端口的范围
    private int endPort;  //分配端口的范围
    private List<User> users;   //用户

    public String getLocalIp() {
        return localIp;
    }

    public void setLocalIp(String localIp) {
        this.localIp = localIp;
    }

    public int getTcpPort() {
        return tcpPort;
    }

    public void setTcpPort(int tcpPort) {
        this.tcpPort = tcpPort;
    }

    public int getHttpPort() {
        return httpPort;
    }

    public void setHttpPort(int httpPort) {
        this.httpPort = httpPort;
    }

    public int getStartPort() {
        return startPort;
    }

    public void setStartPort(int startPort) {
        this.startPort = startPort;
    }

    public int getEndPort() {
        return endPort;
    }

    public void setEndPort(int endPort) {
        this.endPort = endPort;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
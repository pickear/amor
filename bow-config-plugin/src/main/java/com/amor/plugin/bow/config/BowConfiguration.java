package com.amor.plugin.bow.config;

import com.amor.core.config.BowConfig;
import com.amor.core.helper.ClassPathResourceHelper;
import com.amor.core.helper.YamlHelper;
import com.amor.core.model.User;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author dylan
 * @time 2017/6/15
 */
public class BowConfiguration implements BowConfig {

    private static BowConfiguration properties = null;
    private String localIp = "127.0.0.1";
    private int tcpPort = 9998;  //tcp监听的端口
    private int httpPort = 8090;  //http监听的端口
    private int httpsPort = 443; //https监听的端口
    private int startPort = 10000; //分配端口的范围
    private int endPort = 20000;  //分配端口的范围
    private String domain; //域名
    private List<User> users = Lists.newArrayList();

    /**
     *
     * @return
     */
    public static synchronized BowConfiguration instance(){
        if(null == properties){
           reInstance();
        }
        return properties;
    }

    /**
     *
     * @return
     */
    public static synchronized BowConfiguration reInstance(){
        BowConfiguration _properties = YamlHelper.load(
                ClassPathResourceHelper.getInputStream("application.yml"),BowConfiguration.class
        );
        properties = _properties;
        return properties;
    }

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

    public int getHttpsPort() {
        return httpsPort;
    }

    public void setHttpsPort(int httpsPort) {
        this.httpsPort = httpsPort;
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

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}

package com.amor.bow.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author dylan
 * @time 2017/6/15
 */
@ConfigurationProperties(
        prefix = "bow"
)
public class BowProxyProperties {

    private String localIp;
    private int tcpPort = 9998;  //tcp监听的端口
    private int httpPort = 8090;  //http监听的端口
    private int httpsPort = 443; //https监听的端口
    private int startPort; //分配端口的范围
    private int endPort;  //分配端口的范围
    private String domain; //域名

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

}

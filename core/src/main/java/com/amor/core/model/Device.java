package com.amor.core.model;

import org.apache.commons.lang3.StringUtils;
import org.msgpack.annotation.Message;
import org.msgpack.annotation.MessagePackOrdinalEnum;

/**设备
 * Created by dylan on 2017/9/30.
 */
@Message
public class Device extends Model{

    /**
     * 远程ip
     */
    private String remoteIp;

    /**
     * 远程端口
     */
    private int remotePort;
    /**
     * 映射的ip
     */
    private String mapIp;

    /**
     * 映射的端口
     */
    private int mapPort;

    /**
     * 二级域名，当protocolType为http或者https时才有值,默认为ID值
     */
    private String subDomain;

    /**
     * 设备状态
     */
    private Status status = Status.OFFLINE;


    /**
     * 设备协议
     */
    private ProtocolType protocolType;

    public Device() {
    }
    public Device(long id) {
        this.setId(id);
    }
    public String getRemoteIp() {
        return remoteIp;
    }

    public void setRemoteIp(String remoteIp) {
        this.remoteIp = remoteIp;
    }

    public int getRemotePort() {
        return remotePort;
    }

    public void setRemotePort(int remotePort) {
        this.remotePort = remotePort;
    }

    public String getMapIp() {
        return mapIp;
    }

    public void setMapIp(String mapIp) {
        this.mapIp = mapIp;
    }

    public int getMapPort() {
        return mapPort;
    }

    public void setMapPort(int mapPort) {
        this.mapPort = mapPort;
    }

    public String getSubDomain() {
        if(StringUtils.isBlank(subDomain)){
            setSubDomain(String.valueOf(getId()));
        }
        return subDomain;
    }

    public void setSubDomain(String subDomain) {
        this.subDomain = subDomain;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public ProtocolType getProtocolType() {
        return protocolType;
    }

    public void setProtocolType(ProtocolType protocolType) {
        this.protocolType = protocolType;
    }

    @MessagePackOrdinalEnum
    public enum Status{
        ONLINE,OFFLINE
    }

    @MessagePackOrdinalEnum
    public enum ProtocolType{
        TCP,HTTP,HTTPS
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Device device = (Device) o;
        return id == device.id;
    }

    @Override
    public int hashCode() {
        return new Long(id).intValue();
    }
}

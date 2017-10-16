package com.amor.common.protocol;

import com.amor.common.model.Device;
import org.msgpack.annotation.Message;

/**
 * Created by dylan on 2017/10/4.
 */
@Message
public class DeviceOnlineProtocol extends AbstractProtocol {

    private String clientId;
    private Device device;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public DeviceOnlineProtocol() {
        super(DeviceOnlineProtocol.class.getName());
    }

}

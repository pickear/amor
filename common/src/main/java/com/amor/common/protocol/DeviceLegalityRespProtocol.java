package com.amor.common.protocol;

import com.amor.common.model.Device;
import org.msgpack.annotation.Message;

import java.util.List;

/**
 * Created by dell on 2017/10/10.
 */
@Message
public class DeviceLegalityRespProtocol extends AbstractProtocol{

    private List<Device> devices;

    public DeviceLegalityRespProtocol() {
        super(DeviceLegalityRespProtocol.class.getName());
    }

    public List<Device> getDevices() {
        return devices;
    }

    public void setDevices(List<Device> devices) {
        this.devices = devices;
    }

}

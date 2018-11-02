package com.amor.core.protocol;

import com.amor.core.model.Device;
import org.msgpack.annotation.Message;

import java.util.List;

/**
 * Created by dell on 2017/10/10.
 */
@Message
public class DeviceLegalityProtocol extends AbstractProtocol{

    private List<Device> devices;

    public DeviceLegalityProtocol() {
        super(DeviceLegalityProtocol.class.getName());
    }

    public List<Device> getDevices() {
        return devices;
    }

    public void setDevices(List<Device> devices) {
        this.devices = devices;
    }

}

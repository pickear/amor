package com.amor.common.protocol;

import com.amor.common.model.Device;
import org.msgpack.annotation.Message;

/**
 * Created by dylan on 2017/10/3.
 */
@Message
public class HttpProtocol extends AbstractProtocol {

    private Device device;
    private String clientId;
    private byte[] msg;

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public byte[] getMsg() {
        return msg;
    }

    public void setMsg(byte[] msg) {
        this.msg = msg;
    }

    public HttpProtocol() {
        super(HttpProtocol.class.getName());
    }
}

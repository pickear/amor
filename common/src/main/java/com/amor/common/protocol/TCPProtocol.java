package com.amor.common.protocol;

import org.msgpack.annotation.Message;

/**
 * Created by dylan on 2017/10/3.
 */
@Message
public class TCPProtocol extends AbstractProtocol {

    private String clientId;
    private byte[] msg;

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

    public TCPProtocol() {
        super(TCPProtocol.class.getName());
    }
}

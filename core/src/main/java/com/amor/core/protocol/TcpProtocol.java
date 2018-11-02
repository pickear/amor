package com.amor.core.protocol;

import org.msgpack.annotation.Message;

/**
 * Created by dylan on 2017/10/3.
 */
@Message
public class TcpProtocol extends AbstractProtocol {

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

    public TcpProtocol() {
        super(TcpProtocol.class.getName());
    }
}

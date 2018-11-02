package com.amor.core.protocol;

import org.msgpack.annotation.Message;

/**
 * Created by dell on 2017/10/10.
 */
@Message
public class HeartBeatProtocol extends AbstractProtocol{

    private String message;

    public HeartBeatProtocol() {
        super(HeartBeatProtocol.class.getName());
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

package com.amor.common.protocol;

import org.msgpack.annotation.Message;

/**
 * Created by dell on 2017/10/10.
 */
@Message
public class AuthcRespProtocol extends AbstractProtocol{

    private String message;

    public AuthcRespProtocol() {
        super(AuthcRespProtocol.class.getName());
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

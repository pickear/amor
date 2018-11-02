package com.amor.core.pack;

import org.msgpack.MessagePack;

/**
 * Created by dell on 2017/10/10.
 */
public final class MessagePackFactory {

    public static MessagePack create(){
        return new MessagePack();
    }

    private MessagePackFactory(){}
}

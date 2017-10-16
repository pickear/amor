package com.amor.common.codec;

import com.amor.common.factory.MessagePackFactory;
import com.amor.common.protocol.AbstractProtocol;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.msgpack.MessagePack;

import java.util.List;

/**解码器
 * Created by dell on 2017/10/10.
 */
public class MessagePackDecoder extends MessageToMessageDecoder<ByteBuf> {


    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        final int length = msg.readableBytes();
        if(length > 0){
            byte[] msgBytes = new byte[length];
            msg.getBytes(msg.readerIndex(),msgBytes,0,length);
            MessagePack messagePack = MessagePackFactory.create();
            AbstractProtocol abstractProtocol = messagePack.read(msgBytes, AbstractProtocol.class);
            Class clazz = Class.forName(abstractProtocol.getEntityClass());
            Object protocol = messagePack.read(msgBytes,clazz);
            out.add(protocol);
            return;
        }
        out.add(msg);
    }
}

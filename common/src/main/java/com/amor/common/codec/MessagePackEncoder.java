package com.amor.common.codec;

import com.amor.core.pack.MessagePackFactory;
import com.amor.core.protocol.AbstractProtocol;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.msgpack.MessagePack;

/**编码器
 * Created by dell on 2017/10/10.
 */
public class MessagePackEncoder extends MessageToByteEncoder<AbstractProtocol> {

    @Override
    protected void encode(ChannelHandlerContext ctx, AbstractProtocol msg, ByteBuf out) throws Exception {
        MessagePack messagePack = MessagePackFactory.create();
        byte[] bytes = messagePack.write(msg);
        out.writeBytes(bytes);
    }
}

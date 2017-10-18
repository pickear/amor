package com.amor.bow.handler;

import com.amor.common.helper.ByteHelper;
import com.amor.common.manager.ChannelManager;
import com.amor.common.protocol.TcpProtocol;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author dylan
 * @time 2017/6/15
 */
public class BowProtocolHandler extends SimpleChannelInboundHandler<TcpProtocol>{

    private Logger logger = LoggerFactory.getLogger(BowProtocolHandler.class);

    @Override
    public void channelRead0(ChannelHandlerContext ctx, TcpProtocol protocol) throws Exception {
        Channel clientChannel = ChannelManager.get(protocol.getClientId());
        if(null != clientChannel && clientChannel.isActive()){
            ByteBuf byteBuf = ByteHelper.byteToByteBuf(protocol.getMsg());
            logger.debug("收到arrow转发过来的映射地址的消息:{},开始转发给客户端:{}",protocol.getMsg(),clientChannel.remoteAddress());
            clientChannel.writeAndFlush(byteBuf)
                    .addListener(new ChannelFutureListener() {
                        @Override
                        public void operationComplete(ChannelFuture future) throws Exception {
                            if (future.isSuccess()) {
                                ctx.channel().read();
                            } else {
                                logger.warn("转发消息给客户端失败，关闭与客户端的连接channel!");
                                future.channel().close();
                            }
                        }
                    });
        }
    }



}

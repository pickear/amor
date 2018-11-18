package com.amor.arrow.handler.tcp;

import com.amor.arrow.manager.MapChannelManager;
import com.amor.common.helper.ByteHelper;
import com.amor.core.protocol.TcpProtocol;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author dylan
 * @time 2017/6/15
 */
public class ArrowTcpProtocolFrontHandler extends SimpleChannelInboundHandler<TcpProtocol>{

    private Logger logger = LoggerFactory.getLogger(ArrowTcpProtocolFrontHandler.class);

    @Override
    public void channelRead0(ChannelHandlerContext ctx, TcpProtocol protocol) throws Exception {

        Channel mapChannel = MapChannelManager.get(protocol.getClientId());
        if (null == mapChannel) {
            logger.error("can not find the mapping");
            return;
        }
        byte[] msg = protocol.getMsg();
        while (!mapChannel.isActive()){
        }
        ByteBuf byteBuf = ByteHelper.byteToByteBuf(msg);
        logger.debug("receive client message from ,relpay to mapping.message is :{},mapping is :{}",protocol.getMsg(),mapChannel.remoteAddress());
        mapChannel.writeAndFlush(byteBuf)
                .addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture future) throws Exception {
                        if (future.isSuccess()) {
                            mapChannel.read();
                        } else {
                            logger.warn("close mapping channel",mapChannel.remoteAddress());
                            mapChannel.close();
                        }
                    }
                });
    }

}

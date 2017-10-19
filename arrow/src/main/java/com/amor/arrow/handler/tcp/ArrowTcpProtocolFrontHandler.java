package com.amor.arrow.handler.tcp;

import com.amor.arrow.manager.MapChannelManager;
import com.amor.common.helper.ByteHelper;
import com.amor.common.protocol.TcpProtocol;
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
            logger.error("连接的客户端与映射地址没建立关联，无法转发消息!");
            return;
        }
        byte[] msg = protocol.getMsg();
        while (!mapChannel.isActive()){
        }
        ByteBuf byteBuf = ByteHelper.byteToByteBuf(msg);
        logger.debug("收到bow转发过来的客户端消息:{},开始转发给映射地址:{}",protocol.getMsg(),mapChannel.remoteAddress());
        mapChannel.writeAndFlush(byteBuf)
                .addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture future) throws Exception {
                        if (future.isSuccess()) {
                            mapChannel.read();
                        } else {
                            logger.warn("与映射地址[{}]的连接被关闭......",mapChannel.remoteAddress());
                            mapChannel.close();
                        }
                    }
                });
    }

}

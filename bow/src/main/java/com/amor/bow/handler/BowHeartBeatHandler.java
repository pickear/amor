package com.amor.bow.handler;

import com.amor.common.channel.HeartBeatChannel;
import com.amor.common.manager.ChannelManager;
import com.amor.core.protocol.HeartBeatProtocol;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author dylan
 * @time 2017/6/15
 */
public class BowHeartBeatHandler extends HeartBeatChannel{
    private final static Logger logger = LoggerFactory.getLogger(BowHeartBeatHandler.class);

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info("客户端退出!");
        ChannelManager.closeOnFlush(ctx.channel());
        ctx.fireChannelInactive();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("连接异常!",cause);
        ChannelManager.closeOnFlush(ctx.channel());
    }

    @Override
    protected void handleData(ChannelHandlerContext context, HeartBeatProtocol msg) {
        logger.debug("接收到arrow的心跳消息" , msg.getMessage());
        context.writeAndFlush(msg);
    }

    @Override
    protected void handleReaderIdle(ChannelHandlerContext context) {
        logger.warn("arrow[{}]心跳超时,关闭arrow",context.channel().remoteAddress());
        ChannelManager.closeOnFlush(context.channel());
    }
}

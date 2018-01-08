package com.amor.arrow.handler;

import com.amor.arrow.manager.MapChannelManager;
import com.amor.arrow.bootstrap.ArrowBootstrap;
import com.amor.common.channel.HeartBeatChannel;
import com.amor.common.manager.ChannelManager;
import com.amor.common.protocol.HeartBeatProtocol;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author dylan
 * @time 2017/6/15
 */
public class ArrowHeartBeatHandler extends HeartBeatChannel{

    private final static Logger logger = LoggerFactory.getLogger(ArrowHeartBeatHandler.class);
    private ArrowBootstrap arrow = new ArrowBootstrap();

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        MapChannelManager.all()
                         .forEach(
                channel -> ChannelManager.closeOnFlush(channel)
        );
        if(arrow.closedArrow){
            logger.info("连接被关闭!");
            ChannelManager.closeOnFlush(ctx.channel());
            return;
        }
        logger.info("连接被关闭，进行重连!");
        arrow.connect();
    }

    @Override
    protected void handleData(ChannelHandlerContext context, HeartBeatProtocol msg) {
        logger.debug("接收到bow的心跳消息" , msg.getMessage());
    }

    @Override
    protected void handleAllIdle(ChannelHandlerContext context) {
        logger.debug("发送ping心跳消息...");
        ping(context);
    }
}

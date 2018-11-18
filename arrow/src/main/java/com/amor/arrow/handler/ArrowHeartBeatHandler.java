package com.amor.arrow.handler;

import com.amor.arrow.listener.event.ReconetionBowEvent;
import com.amor.arrow.manager.MapChannelManager;
import com.amor.common.channel.HeartBeatChannel;
import com.amor.common.manager.ChannelManager;
import com.amor.core.listener.event.EventPublisher;
import com.amor.core.protocol.HeartBeatProtocol;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author dylan
 * @time 2017/6/15
 */
public class ArrowHeartBeatHandler extends HeartBeatChannel{

    private final static Logger logger = LoggerFactory.getLogger(ArrowHeartBeatHandler.class);
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        MapChannelManager.all()
                         .forEach(
                channel -> ChannelManager.closeOnFlush(channel)
        );
       /* if(arrow.closedArrow){
            logger.info("连接被关闭!");
            ChannelManager.closeOnFlush(ctx.channel());
            return;
        }*/
        logger.info("channel closed,reconnect!");
        EventPublisher.postEvent(new ReconetionBowEvent());
    }

    @Override
    protected void handleData(ChannelHandlerContext context, HeartBeatProtocol msg) {
        logger.debug("receive bow's heatbeat messge" , msg.getMessage());
    }

    @Override
    protected void handleAllIdle(ChannelHandlerContext context) {
        logger.debug("send ping message");
        ping(context);
    }
}

package com.amor.bow.handler;

import com.amor.common.manager.ChannelManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by dylan on 2017/10/8.
 */
public class BowChannelManagerHandler extends ChannelInboundHandlerAdapter {

    private final static Logger logger = LoggerFactory.getLogger(BowChannelManagerHandler.class);
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("save channel[{}]",ctx.channel().id().asLongText());
        ChannelManager.save(ctx.channel());
        ctx.fireChannelActive();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info("remove channel[{}]",ctx.channel().id().asLongText());
        ChannelManager.remove(ctx.channel());
        ctx.fireChannelInactive();
    }
}

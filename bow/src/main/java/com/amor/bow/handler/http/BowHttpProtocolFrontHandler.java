package com.amor.bow.handler.http;

import com.amor.bow.helper.SpringBeanHolder;
import com.amor.bow.repository.DeviceManager;
import com.amor.bow.repository.impl.DeviceManagerImpl;
import com.amor.common.manager.ChannelManager;
import io.netty.channel.*;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author dylan
 * @time 2017/6/15
 */
@Sharable
public class BowHttpProtocolFrontHandler extends ChannelInboundHandlerAdapter{

    private Logger logger = LoggerFactory.getLogger(BowHttpProtocolFrontHandler.class);
    private DeviceManager deviceManager = SpringBeanHolder.getBean(DeviceManagerImpl.class);
    private Channel outboundChannel;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("客户端[{}]连接!",ctx.channel().id().asLongText());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        logger.info(msg.toString());
        HttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        ctx.writeAndFlush(response).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                logger.info("aaaaaaaaaaa");
            }
        });
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        logger.info("客户端退出，关闭客户端连接!");
        ChannelManager.closeOnFlush(ctx.channel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        logger.error("与客户端连接异常!",cause);
        ChannelManager.closeOnFlush(ctx.channel());
    }

}

package com.amor.bow.handler.http;

import com.amor.bow.handler.BowChannelManagerHandler;
import com.amor.core.context.ConfigurableContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

/**
 * @author dylan
 * @time 2017/6/15
 */
public class BowHttpChannelInitalizer extends ChannelInitializer<SocketChannel> {


    @Override
    protected void initChannel(SocketChannel socketChannel){
        socketChannel.pipeline().addLast(
                new BowChannelManagerHandler(),
                new BowHttpProtocolFrontHandler()
        );
    }
}

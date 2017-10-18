package com.amor.bow.handler.http;

import com.amor.bow.handler.*;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * @author dylan
 * @time 2017/6/15
 */
public class BowHttpChannelInitalizer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        /*socketChannel.pipeline().addLast("httpCodec",new HttpServerCodec());
        socketChannel.pipeline().addLast("httpObject",new HttpObjectAggregator(65535));*/
        socketChannel.pipeline().addLast("httpObject",new HttpResponseEncoder());
        socketChannel.pipeline().addLast("managerHandler",new BowChannelManagerHandler());
        socketChannel.pipeline().addLast("frontHandler",new BowHttpProtocolFrontHandler());
    }
}

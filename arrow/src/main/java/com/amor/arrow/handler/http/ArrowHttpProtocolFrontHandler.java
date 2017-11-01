package com.amor.arrow.handler.http;

import com.amor.arrow.manager.MapChannelManager;
import com.amor.common.helper.ByteHelper;
import com.amor.common.model.Device;
import com.amor.common.protocol.HttpProtocol;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.handler.codec.http.*;
import io.netty.handler.ssl.SslHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author dylan
 * @time 2017/6/15
 */
public class ArrowHttpProtocolFrontHandler extends SimpleChannelInboundHandler<ByteBuf>{

    private Logger logger = LoggerFactory.getLogger(ArrowHttpProtocolFrontHandler.class);

    @Override
    public void channelRead0(ChannelHandlerContext ctx, ByteBuf protocol) throws Exception {

        final Channel inboundChannel = ctx.channel();
        final Channel _mapChannel = inboundChannel.attr(HttpProtocolUnWrapper.MAP_CHANNEL).get();
        if(_mapChannel == null || !_mapChannel.isActive()){
            logger.error("http server channel is not active......");
            return;
        }
        _mapChannel.writeAndFlush(protocol)
                .addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture future) throws Exception {
                        if (future.isSuccess()) {
                            _mapChannel.read();
                        } else {
                            logger.warn("转发消息到http服务器[{}]失败......",_mapChannel.remoteAddress());
                        }
                    }
                });
    }

}

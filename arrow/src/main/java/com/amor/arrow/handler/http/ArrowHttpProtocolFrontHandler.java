package com.amor.arrow.handler.http;

import com.amor.arrow.manager.MapChannelManager;
import com.amor.common.helper.ByteHelper;
import com.amor.common.model.Device;
import com.amor.common.protocol.HttpProtocol;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpRequestEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author dylan
 * @time 2017/6/15
 */
public class ArrowHttpProtocolFrontHandler extends SimpleChannelInboundHandler<HttpProtocol>{

    private Logger logger = LoggerFactory.getLogger(ArrowHttpProtocolFrontHandler.class);

    @Override
    public void channelRead0(ChannelHandlerContext ctx, HttpProtocol protocol) throws Exception {

        final Channel inboundChannel = ctx.channel();
        Channel mapChannel = MapChannelManager.get(protocol.getClientId());
        if (null == mapChannel || !mapChannel.isActive()) {
            Device device = protocol.getDevice();
            logger.info("与bow建立连接通关成功，开始建立映射[{}:{}]连接!", device.getMapIp(), device.getMapPort());
            Bootstrap bootstrap = new Bootstrap();
            ChannelFuture channelFuture = bootstrap.group(new NioEventLoopGroup())
                    .channel(inboundChannel.getClass())
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
                    /*.option(ChannelOption.AUTO_READ, false)*/
                    .handler(new ChannelInitializer() {
                        @Override
                        protected void initChannel(Channel ch) throws Exception {
                            ch.pipeline().addLast(
                                                new ArrowHttpProtocolBackendHandler(protocol, inboundChannel)
                                        );
                        }
                    })
                    .connect(device.getMapIp(), device.getMapPort());
            mapChannel = channelFuture.channel();
            MapChannelManager.create(protocol.getClientId(),mapChannel);
            channelFuture.addListener(new ChannelFutureListener() {
                public void operationComplete(ChannelFuture future) {
                    if (future.isSuccess()) {
                        inboundChannel.read();
                    } else {
                        logger.warn("与http服务器映射失败，关闭映射连接......");
                        future.channel().close();
                    }
                }
            });
        }
        final Channel _mapChannel = mapChannel;
        while (!mapChannel.isActive()){
        }
        byte[] msg = protocol.getMsg();
        ByteBuf byteBuf = ByteHelper.byteToByteBuf(msg);
        logger.info("收到bow转发过来的客户端消息:{},开始转发给映射地址:{}",protocol.getMsg(),mapChannel.remoteAddress());
        mapChannel.writeAndFlush(byteBuf)
                .addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture future) throws Exception {
                        if (future.isSuccess()) {
                            _mapChannel.read();
                        } else {
                            logger.warn("转发消息到http服务器[{}]失败......",_mapChannel.remoteAddress());
                            _mapChannel.close();
                        }
                    }
                });
    }

}

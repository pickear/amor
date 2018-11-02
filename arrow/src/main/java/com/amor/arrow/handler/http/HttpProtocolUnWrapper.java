package com.amor.arrow.handler.http;

import com.amor.arrow.manager.MapChannelManager;
import com.amor.common.helper.ByteHelper;
import com.amor.core.model.Device;
import com.amor.core.protocol.HttpProtocol;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.util.AttributeKey;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by dell on 2017/11/1.
 */
public class HttpProtocolUnWrapper extends MessageToMessageDecoder<HttpProtocol> {

    private Logger logger = LoggerFactory.getLogger(ArrowHttpProtocolFrontHandler.class);
    public static AttributeKey<Channel> MAP_CHANNEL = AttributeKey.valueOf("map_channel");

    @Override
    protected void decode(ChannelHandlerContext ctx, HttpProtocol protocol, List<Object> out) throws Exception {

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
                                    new HttpContentCompressor(),
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
        while (!mapChannel.isActive()){
        }
        inboundChannel.attr(MAP_CHANNEL).set(mapChannel);
        byte[] msg = protocol.getMsg();
        ByteBuf byteBuf = ByteHelper.byteToByteBuf(msg);
        ReferenceCountUtil.retain(byteBuf);
        out.add(byteBuf);
    }
}

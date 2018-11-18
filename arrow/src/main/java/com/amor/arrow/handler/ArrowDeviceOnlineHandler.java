package com.amor.arrow.handler;

import com.amor.arrow.handler.tcp.ArrowTcpProtocolBackendHandler;
import com.amor.arrow.manager.MapChannelManager;
import com.amor.core.model.Device;
import com.amor.core.protocol.DeviceOnlineProtocol;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author dylan
 * @time 2017/6/15
 */
public class ArrowDeviceOnlineHandler extends SimpleChannelInboundHandler<DeviceOnlineProtocol>{

    private Logger logger = LoggerFactory.getLogger(ArrowDeviceOnlineHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DeviceOnlineProtocol protocol) throws Exception {

        final Channel inboundChannel = ctx.channel();
        Channel mapChannel = MapChannelManager.get(protocol.getClientId());
        if (null == mapChannel) {
            Device device = protocol.getDevice();
            logger.info("connect bow success,mapping[{}:{}]", device.getMapIp(), device.getMapPort());
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
                            ch.pipeline()
                                    .addLast(
                                           /* new LoggingHandler(LogLevel.INFO),*/
                                            new ArrowTcpProtocolBackendHandler(protocol, inboundChannel)
                                    );
                        }
                    })
                    .connect(device.getMapIp(), device.getMapPort());
            mapChannel = channelFuture.channel();
            MapChannelManager.create(protocol.getClientId(),mapChannel);
            channelFuture.addListener(new ChannelFutureListener() {
                public void operationComplete(ChannelFuture future) {
                    if (future.isSuccess()) {
                        // connection complete start to read first data
                        inboundChannel.read();
                    } else {
                        // Close the connection if the connection attempt has failed.
                        logger.warn("mapping fail,close channel");
                        future.channel().close();
                    }
                }
            });

        }
    }
}

package com.amor.bow.handler;

import com.amor.bow.helper.SpringBeanHolder;
import com.amor.bow.repository.DeviceManager;
import com.amor.bow.repository.impl.DeviceManagerImpl;
import com.amor.common.helper.ByteHelper;
import com.amor.common.manager.ChannelManager;
import com.amor.common.manager.DeviceChannelManager;
import com.amor.common.model.Device;
import com.amor.common.protocol.DeviceOnlineProtocol;
import com.amor.common.protocol.TCPProtocol;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.ChannelHandler.Sharable;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author dylan
 * @time 2017/6/15
 */
@Sharable
public class BowTCPProtocolFrontHandler extends ChannelInboundHandlerAdapter{

    private Logger logger = LoggerFactory.getLogger(BowTCPProtocolFrontHandler.class);
    private DeviceManager deviceManager = SpringBeanHolder.getBean(DeviceManagerImpl.class);
    private Channel outboundChannel;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        final Channel inboundChannel = ctx.channel();
        String channelId = inboundChannel.id().asLongText();
        String localAddress = ctx.channel().localAddress().toString();
        int port = Integer.valueOf(StringUtils.split(localAddress,":")[1]);
        Device device = deviceManager.getByPort(port);

        DeviceChannelManager.DeviceChannelRelastion deviceChannelRelastion = DeviceChannelManager.get(device.getId());
        outboundChannel = deviceChannelRelastion.getBowChannel();
        if(outboundChannel.isActive()){
            DeviceOnlineProtocol protocol = new DeviceOnlineProtocol();
            protocol.setDevice(device);
            protocol.setClientId(channelId);
            logger.info("客户端已连接，通知arrow主动与bow建立连接通道!",channelId);
            outboundChannel.writeAndFlush(protocol);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(outboundChannel.isActive()){
            String channelId = ctx.channel().id().asLongText();
            TCPProtocol protocol = new TCPProtocol();
            protocol.setClientId(channelId);
            ByteBuf byteBuf = (ByteBuf) msg;
            protocol.setMsg(ByteHelper.byteBufToByte(byteBuf));
            logger.info("读取到客户端消息:{},并进行转发给bow:{}",msg,outboundChannel.remoteAddress());
            outboundChannel.writeAndFlush(protocol)
                    .addListener(new ChannelFutureListener() {
                        @Override
                        public void operationComplete(ChannelFuture future) throws Exception {
                            if (future.isSuccess()) {
                                ctx.channel().read();
                            } else {
                                logger.warn("client channel 被关闭!!!");
                                future.channel().close();
                            }
                        }
                    });
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        logger.info("客户端退出，关闭客户端连接!");
        ChannelManager.closeOnFlush(ctx.channel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
       // ChannelManager.closeOnFlush(ctx.channel());
    }

}

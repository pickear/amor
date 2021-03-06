package com.amor.bow.handler.tcp;

import com.amor.common.helper.ByteHelper;
import com.amor.common.manager.ChannelManager;
import com.amor.common.manager.DeviceChannelManager;
import com.amor.core.context.ConfigurableContext;
import com.amor.core.context.ContextHolder;
import com.amor.core.model.Device;
import com.amor.core.protocol.DeviceOnlineProtocol;
import com.amor.core.protocol.TcpProtocol;
import io.netty.buffer.ByteBuf;
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
public class BowTcpProtocolFrontHandler extends ChannelInboundHandlerAdapter{

    private Logger logger = LoggerFactory.getLogger(BowTcpProtocolFrontHandler.class);
    private ConfigurableContext context = (ConfigurableContext) ContextHolder.getContext();
    private Channel bowChannel;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        final Channel inboundChannel = ctx.channel();
        String channelId = inboundChannel.id().asLongText();
        String localAddress = ctx.channel().localAddress().toString();
        int port = Integer.valueOf(StringUtils.split(localAddress,":")[1]);
        Device device = context.getBowConfig().getDeviceByPort(port);

        DeviceChannelManager.DeviceChannelRelastion deviceChannelRelastion = DeviceChannelManager.get(device.getId());
        bowChannel = deviceChannelRelastion.getBowChannel();
        if(bowChannel.isActive()){
            DeviceOnlineProtocol protocol = new DeviceOnlineProtocol();
            protocol.setDevice(device);
            protocol.setClientId(channelId);
            logger.info("客户端已连接，通知arrow主动与bow建立连接通道!",channelId);
            bowChannel.writeAndFlush(protocol);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(bowChannel.isActive()){
            String channelId = ctx.channel().id().asLongText();
            TcpProtocol protocol = new TcpProtocol();
            protocol.setClientId(channelId);
            ByteBuf byteBuf = (ByteBuf) msg;
            protocol.setMsg(ByteHelper.byteBufToByte(byteBuf));
            logger.debug("读取到客户端消息:{},通过bow转发给给arrow:{}",msg, bowChannel.remoteAddress());
            bowChannel.writeAndFlush(protocol)
                    .addListener(new ChannelFutureListener() {
                        @Override
                        public void operationComplete(ChannelFuture future) throws Exception {
                            if (future.isSuccess()) {
                                ctx.channel().read();
                            } else {
                                logger.warn("通过bow转发消息给arrow失败，关闭bow与arrow的通道!");
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
        logger.error("客户端与bow连接异常",cause);
       // ChannelManager.closeOnFlush(ctx.channel());
    }

}

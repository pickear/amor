package com.amor.bow.handler;

import com.amor.bow.handler.tcp.BowTcpChannelInitalizer;
import com.amor.bow.helper.InetAddressHelper;
import com.amor.common.helper.AttributeMapConstant;
import com.amor.core.context.ConfigurableContext;
import com.amor.core.context.ContextHolder;
import com.amor.core.helper.GsonHelper;
import com.amor.common.manager.ChannelManager;
import com.amor.common.manager.DeviceChannelManager;
import com.amor.core.model.Device;
import com.amor.core.protocol.DeviceLegalityProtocol;
import com.amor.core.protocol.DeviceLegalityRespProtocol;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.Attribute;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**设备合法性效验处理器
 * Created by dylan on 2017/10/8.
 */
public class BowDeviceLegalityHandler extends SimpleChannelInboundHandler<DeviceLegalityProtocol> {

    private final static Logger logger = LoggerFactory.getLogger(BowDeviceLegalityHandler.class);
    private ServerBootstrap tcpBootstrap = new ServerBootstrap();
    private ConfigurableContext context = (ConfigurableContext) ContextHolder.getContext();
    private EventLoopGroup tcpEventExecutors = new NioEventLoopGroup();
    private List<Device> devices = new ArrayList<>();
    {
        tcpBootstrap.group(tcpEventExecutors,tcpEventExecutors)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new BowTcpChannelInitalizer());

    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DeviceLegalityProtocol protocol) throws Exception {

        Attribute<String> userAttribute =ctx.channel().attr(AttributeMapConstant.USER_KEY);
        String username = userAttribute.get();
        if(StringUtils.isBlank(username)){
            logger.error("can not receive user info!");
        }

        final Channel inboundChannel = ctx.channel();
        devices = protocol.getDevices();
        List<Device> _devices = context.getBowConfig().getDeviceByUsername(username);

        List<Device> noContainsDevices = devices.stream()
                                                .filter(device->!_devices.contains(device))
                                                .collect(Collectors.toList());
        if(null != noContainsDevices && !noContainsDevices.isEmpty()){
            logger.error("user[{}] do not contain the device[{}]，close channel!",username, GsonHelper.toJson(noContainsDevices));
            DeviceLegalityRespProtocol deviceLegalityRespProtocol = new DeviceLegalityRespProtocol();
            deviceLegalityRespProtocol.setDevices(noContainsDevices);
            ctx.writeAndFlush(deviceLegalityRespProtocol);
            ChannelManager.closeOnFlush(ctx.channel());
            return;
        }

        List<Device> onlineDevices = _devices.stream()
                                             .filter(device -> devices.contains(device) && device.getStatus() == Device.Status.ONLINE)
                                             .collect(Collectors.toList());
        if(null != onlineDevices && !onlineDevices.isEmpty()){
            logger.error("device had online", GsonHelper.toJson(noContainsDevices));
            DeviceLegalityRespProtocol deviceLegalityRespProtocol = new DeviceLegalityRespProtocol();
            deviceLegalityRespProtocol.setDevices(onlineDevices);
            ctx.writeAndFlush(deviceLegalityRespProtocol);
            ChannelManager.closeOnFlush(ctx.channel());
            return;
        }

        ctx.writeAndFlush(new DeviceLegalityRespProtocol());
        logger.info("user's device check success，listen all device port!",username);
        devices.stream()
                .forEach(device -> {
                    long deviceId = device.getId();
                    Device _device = _devices.stream()
                                             .filter(device1 -> deviceId == device1.getId())
                                             .findFirst()
                                             .orElse(null);

                    int port = _device.getRemotePort();

                    if(_device.getProtocolType() != Device.ProtocolType.TCP){
                        DeviceChannelManager.create(new DeviceChannelManager.DeviceChannelRelastion(deviceId,null,inboundChannel));
                        return;
                    }
                    boolean portUsed = InetAddressHelper.localPortUsed(port);
                    if(portUsed){
                        logger.error("port[{}] had be used!",port);
                        return;
                    }
                    logger.info("bind port[{}]",port);

                    ChannelFuture channelFuture = tcpBootstrap.bind(new InetSocketAddress(port));
                    Channel channel = channelFuture.channel();
                    DeviceChannelManager.create(new DeviceChannelManager.DeviceChannelRelastion(deviceId,channel,inboundChannel));
                });
    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        devices.forEach(device -> {
            Device _device = context.getBowConfig().getDeviceById(device.getId());
            logger.info("device[{}] offline,close channel",_device.getId(),_device.getRemotePort());
            DeviceChannelManager.DeviceChannelRelastion relastion = DeviceChannelManager.get(device.getId());
            if(null != relastion){
                ChannelManager.closeOnFlush(relastion.getBowChannel());
                ChannelManager.closeOnFlush(relastion.getClientChannel());
            }
        });
    }
}

package com.amor.bow.handler;

import com.amor.bow.handler.tcp.BowTcpChannelInitalizer;
import com.amor.bow.handler.tcp.BowTcpProtocolFrontHandler;
import com.amor.bow.helper.PortHelper;
import com.amor.bow.helper.SpringBeanHolder;
import com.amor.bow.repository.DeviceManager;
import com.amor.bow.repository.impl.DeviceManagerImpl;
import com.amor.common.helper.AttributeMapConstant;
import com.amor.common.manager.ChannelManager;
import com.amor.common.helper.GsonHelper;
import com.amor.common.manager.DeviceChannelManager;
import com.amor.common.model.Device;
import com.amor.common.protocol.DeviceLegalityProtocol;
import com.amor.common.protocol.DeviceLegalityRespProtocol;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
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
    private DeviceManager deviceManager = SpringBeanHolder.getBean(DeviceManagerImpl.class);
    private static ServerBootstrap bootstrap = new ServerBootstrap();
    private static EventLoopGroup eventExecutors = new NioEventLoopGroup();
    private List<Device> devices = new ArrayList<>();
    static {
        bootstrap.group(eventExecutors,eventExecutors)
                .channel(NioServerSocketChannel.class)
                .childHandler(new BowTcpChannelInitalizer());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DeviceLegalityProtocol protocol) throws Exception {

        Attribute<String> userAttribute =ctx.channel().attr(AttributeMapConstant.USER_KEY);
        String username = userAttribute.get();
        if(StringUtils.isBlank(username)){
            logger.error("获取不到用户名，异常!");
        }

        final Channel inboundChannel = ctx.channel();
        devices = protocol.getDevices();
        List<Device> _devices = deviceManager.getByUsername(username);

        List<Device> noContainsDevices = devices.stream()
                                                .filter(device->!_devices.contains(device))
                                                .collect(Collectors.toList());
        if(null != noContainsDevices && !noContainsDevices.isEmpty()){
            logger.error("用户[{}]不包含下面设备[{}]，校验失败，关闭arrow连接!",username, GsonHelper.toJson(noContainsDevices));
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
            logger.error("下面设备[{}]已在线!", GsonHelper.toJson(noContainsDevices));
            DeviceLegalityRespProtocol deviceLegalityRespProtocol = new DeviceLegalityRespProtocol();
            deviceLegalityRespProtocol.setDevices(onlineDevices);
            ctx.writeAndFlush(deviceLegalityRespProtocol);
            ChannelManager.closeOnFlush(ctx.channel());
            return;
        }

        ctx.writeAndFlush(new DeviceLegalityRespProtocol());
        logger.info("用户[{}]的设备校验成功，开始监听设备的外网端口!",username);
        devices.stream()
                .forEach(device -> {
                    long deviceId = device.getId();
                    Device _device = _devices.stream()
                                             .filter(device1 -> deviceId == device1.getId())
                                             .findFirst()
                                             .orElse(null);

                    int port = _device.getRemotePort();
                    boolean portUsed = PortHelper.localPortUsed(port);
                    if(portUsed){
                        logger.error("端口[{}]已被占用，无法监听该端口!",port);
                        return;
                    }
                    logger.info("绑定端口[{}]",port);

                    ChannelFuture channelFuture = bootstrap.bind(new InetSocketAddress(port));
                    Channel channel = channelFuture.channel();
                    DeviceChannelManager.create(new DeviceChannelManager.DeviceChannelRelastion(deviceId,channel,inboundChannel));
                });
    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        devices.forEach(device -> {
            Device _device = deviceManager.get(device.getId());
            logger.info("设备[{}]离线，关闭设备所监听的端口[{}]...",_device.getId(),_device.getRemotePort());
            DeviceChannelManager.DeviceChannelRelastion relastion = DeviceChannelManager.get(device.getId());
            if(null != relastion){
                ChannelManager.closeOnFlush(relastion.getBowChannel());
                ChannelManager.closeOnFlush(relastion.getClientChannel());
            }
        });
    }
}

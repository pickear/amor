package com.amor.arrow.handler;

import com.amor.arrow.bootstrap.ArrowBootstrap;
import com.amor.common.manager.ChannelManager;
import com.amor.core.helper.GsonHelper;
import com.amor.core.protocol.DeviceLegalityRespProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**设备合法性效验处理器
 * Created by dylan on 2017/10/8.
 */
public class ArrowDeviceLegalityHandler extends SimpleChannelInboundHandler<DeviceLegalityRespProtocol> {

    private final static Logger logger = LoggerFactory.getLogger(ArrowDeviceLegalityHandler.class);

    private ArrowBootstrap arrow = new ArrowBootstrap();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DeviceLegalityRespProtocol protocol) throws Exception {
        if(null != protocol.getDevices() && !protocol.getDevices().isEmpty()){
            logger.error("设备[{}]不存在或已在线,设备校验失败，关闭arrow!如果设备不存在，" +
                    "请先申请设备映射。如果设备已在其他arrow配置,请先删该设备在其他arrow的配置。", GsonHelper.toJson(protocol.getDevices()));
            this.arrow.closed();
            ChannelManager.closeOnFlush(ctx.channel());
        }else{
            logger.info("设备校验成功!");
        }
    }

}

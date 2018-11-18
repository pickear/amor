package com.amor.arrow.handler;

import com.amor.arrow.bootstrap.ArrowBootstrap;
import com.amor.arrow.listener.event.CloseEvent;
import com.amor.common.manager.ChannelManager;
import com.amor.core.helper.GsonHelper;
import com.amor.core.listener.event.ApplicationStartOverEvent;
import com.amor.core.listener.event.EventPublisher;
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

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DeviceLegalityRespProtocol protocol){
        if(null != protocol.getDevices() && !protocol.getDevices().isEmpty()){
            logger.error("deive[{}] not exit or offline", GsonHelper.toJson(protocol.getDevices()));
            EventPublisher.postEvent(new CloseEvent());
            ChannelManager.closeOnFlush(ctx.channel());
        }else{
            logger.info("check device success");
        }
    }

}

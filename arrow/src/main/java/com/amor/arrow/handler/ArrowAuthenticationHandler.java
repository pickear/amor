package com.amor.arrow.handler;

import com.amor.common.channel.AuthenticationHandler;
import com.amor.common.manager.ChannelManager;
import com.amor.core.context.ConfigurableContext;
import com.amor.core.protocol.AuthcProtocol;
import com.amor.core.protocol.AuthcRespProtocol;
import com.amor.core.protocol.DeviceLegalityProtocol;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author dylan
 * @time 2017/6/15
 */
public class ArrowAuthenticationHandler extends AuthenticationHandler<AuthcRespProtocol>{

    private final static Logger logger = LoggerFactory.getLogger(ArrowAuthenticationHandler.class);

    private ConfigurableContext context;

    public ArrowAuthenticationHandler(ConfigurableContext context) {
        this.context = context;
    }

    @Override
    public void channelActive(final ChannelHandlerContext ctx){
        AuthcProtocol protocol = new AuthcProtocol();
        protocol.setUsername(context.getArrowConfig().getUsername());
        protocol.setPassword(context.getArrowConfig().getPassword());
        protocol.setDevices(context.getArrowConfig().getDevices());
        logger.info("用户名[{}],密码[{}]，开始认证!",protocol.getUsername(),protocol.getPassword());
        ctx.writeAndFlush(protocol);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, AuthcRespProtocol protocol){
        if(StringUtils.equals(protocol.getMessage(),AUTHC_FAILED)){
            logger.warn("认证失败，用户或密码不正确，客户端退出。");
            ChannelManager.closeOnFlush(channelHandlerContext.channel());
            return;
        }
        logger.info("认证成功，开始检验device有效性!");
        DeviceLegalityProtocol deviceLegalityProtocol = new DeviceLegalityProtocol();
        deviceLegalityProtocol.setDevices(context.getArrowConfig().getDevices());
        channelHandlerContext.writeAndFlush(deviceLegalityProtocol);
    }
}

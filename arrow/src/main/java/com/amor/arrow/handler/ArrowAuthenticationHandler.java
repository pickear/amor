package com.amor.arrow.handler;

import com.amor.arrow.config.ArrowProperties;
import com.amor.arrow.helper.SpringBeanHolder;
import com.amor.arrow.proxy.ArrowProxy;
import com.amor.common.channel.AuthenticationHandler;
import com.amor.common.manager.ChannelManager;
import com.amor.common.model.Arrow;
import com.amor.common.protocol.AuthcProtocol;
import com.amor.common.protocol.AuthcRespProtocol;
import com.amor.common.protocol.DeviceLegalityProtocol;
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

    private ArrowProxy arrow = SpringBeanHolder.getBean(ArrowProxy.class);
    private ArrowProperties properties = SpringBeanHolder.getBean(ArrowProperties.class);

    @Override
    public void channelActive(final ChannelHandlerContext ctx) throws Exception {
        Arrow arrow = new Arrow();
        arrow.setUsername(properties.getUsername());
        arrow.setPassword(properties.getPassword());
        arrow.setDevices(properties.getDevices());
        AuthcProtocol protocol = new AuthcProtocol();
        protocol.setUsername(properties.getUsername());
        protocol.setPassword(properties.getPassword());
        protocol.setDevices(properties.getDevices());
        logger.info("用户名[{}],密码[{}]，开始认证!",arrow.getUsername(),arrow.getPassword());
        ctx.writeAndFlush(protocol);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, AuthcRespProtocol protocol) throws Exception {
        if(StringUtils.equals(protocol.getMessage(),AUTHC_FAILED)){
            logger.warn("认证失败，用户或密码不正确，客户端退出。");
            arrow.closed();
            ChannelManager.closeOnFlush(channelHandlerContext.channel());
            return;
        }
        logger.info("认证成功，开始检验device有效性!");
        DeviceLegalityProtocol deviceLegalityProtocol = new DeviceLegalityProtocol();
        deviceLegalityProtocol.setDevices(properties.getDevices());
        channelHandlerContext.writeAndFlush(deviceLegalityProtocol);
    }
}

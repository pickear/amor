package com.amor.bow.handler;

import com.amor.bow.repository.UserManager;
import com.amor.bow.repository.impl.UserManagerImpl;
import com.amor.common.channel.AuthenticationHandler;
import com.amor.common.helper.AttributeMapConstant;
import com.amor.common.manager.ChannelManager;
import com.amor.core.model.User;
import com.amor.core.protocol.AuthcProtocol;
import com.amor.core.protocol.AuthcRespProtocol;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.Attribute;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**用户密码校验处理器
 * @author dylan
 * @time 2017/6/15
 */
public class BowAuthenticationHandler extends AuthenticationHandler<AuthcProtocol>{

    private Logger logger = LoggerFactory.getLogger(BowAuthenticationHandler.class);
    private UserManager userManager = new UserManagerImpl();

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, AuthcProtocol protocol) throws Exception {

        Attribute<String> userAttribute =channelHandlerContext.channel().attr(AttributeMapConstant.USER_KEY);
        String username = userAttribute.get();
        if(StringUtils.isBlank(username)){
            userAttribute.setIfAbsent(protocol.getUsername());
        }

        User user = userManager.getByName(protocol.getUsername());

        AuthcRespProtocol authcRespProtocol = new AuthcRespProtocol();
        if(null == user || !StringUtils.equals(user.getPassword(),protocol.getPassword())){
            logger.warn("用户[{}]登录失败，关闭arrow的连接!",protocol.getUsername());
            authcRespProtocol.setMessage(AUTHC_FAILED);
            channelHandlerContext.writeAndFlush(authcRespProtocol);
            ChannelManager.closeOnFlush(channelHandlerContext.channel());
            return;
        }
        authcRespProtocol.setMessage(AUTHC_SUCCESS);
        channelHandlerContext.writeAndFlush(authcRespProtocol).addListener(new ChannelFutureListener() {
            public void operationComplete(ChannelFuture future) {
                if (future.isSuccess()) {
                    future.channel().read();
                } else {
                    logger.warn("将认证成功的消息回写给arrow失败，关闭bow与arrow的channel!");
                    future.channel().close();
                }
            }
        });
        logger.info("用户[{}]登录成功!",protocol.getUsername());
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ChannelManager.closeOnFlush(ctx.channel());
    }
}

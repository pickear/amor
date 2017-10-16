package com.amor.common.channel;

import io.netty.channel.SimpleChannelInboundHandler;

/**用户密码校验处理器
 * @author dylan
 * @time 2017/6/15
 */
public abstract class AuthenticationHandler<T> extends SimpleChannelInboundHandler<T>{

    public final static String AUTHC_SUCCESS = "authc:success";
    public final static String AUTHC_FAILED = "authc:failed";

}

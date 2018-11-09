package com.amor.arrow.handler;

import com.amor.arrow.bootstrap.ArrowBootstrap;
import com.amor.arrow.handler.http.ArrowHttpProtocolFrontHandler;
import com.amor.arrow.handler.http.HttpProtocolUnWrapper;
import com.amor.arrow.handler.tcp.ArrowTcpProtocolFrontHandler;
import com.amor.common.codec.MessagePackDecoder;
import com.amor.common.codec.MessagePackEncoder;
import com.amor.core.context.ConfigurableContext;
import com.amor.core.context.Context;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * @author dylan
 * @time 2017/6/15
 */
public class ArrowChannelInitalizer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline()
                     .addLast(
                             /*new LoggingHandler(LogLevel.INFO),*/
                             /*new ObjectDecoder(Integer.MAX_VALUE,ClassResolvers.weakCachingResolver(this.getClass().getClassLoader())),
                             new ObjectEncoder(),*/
                             new LengthFieldBasedFrameDecoder(1024*1024,0,3,0,3),
                             new MessagePackDecoder(),
                             new LengthFieldPrepender(3),
                             new MessagePackEncoder(),
                             new IdleStateHandler(0,0,5),
                             new ArrowHeartBeatHandler(),
                             new ArrowAuthenticationHandler(),
                             new ArrowDeviceLegalityHandler(),
                             new ArrowDeviceOnlineHandler(),
                             new ArrowTcpProtocolFrontHandler(),
                             new HttpProtocolUnWrapper(),
                             new ArrowHttpProtocolFrontHandler()
                     );
    }
}

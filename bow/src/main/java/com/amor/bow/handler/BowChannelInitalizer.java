package com.amor.bow.handler;

import com.amor.bow.handler.http.BowHttpProtocolBackendHandler;
import com.amor.bow.handler.tcp.BowTcpProtocolBackendHandler;
import com.amor.common.codec.MessagePackDecoder;
import com.amor.common.codec.MessagePackEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * @author dylan
 * @time 2017/6/15
 */
public class BowChannelInitalizer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline()
                     .addLast(
                             /*new LoggingHandler(LogLevel.WARN),*/
                             new BowChannelManagerHandler(),
                             /*new ObjectDecoder(Integer.MAX_VALUE,
                                     ClassResolvers.weakCachingResolver(this.getClass().getClassLoader())),
                             new ObjectEncoder(),*/
                             new LengthFieldBasedFrameDecoder(1024*1024,0,2,0,2),
                             new MessagePackDecoder(),
                             new LengthFieldPrepender(2),
                             new MessagePackEncoder(),
                             new IdleStateHandler(10,0,0),
                             new BowHeartBeatHandler(),
                             new BowAuthenticationHandler(),
                             new BowDeviceLegalityHandler(),
                             new BowTcpProtocolBackendHandler(),
                             new BowHttpProtocolBackendHandler()
                     );
    }
}

package com.amor.bow.handler.tcp;

import com.amor.bow.handler.*;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

/**
 * @author dylan
 * @time 2017/6/15
 */
public class BowTcpChannelInitalizer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline()
                     .addLast(
                             /* new LoggingHandler(LogLevel.INFO),*/
                             new BowChannelManagerHandler(),
                             new BowTcpProtocolFrontHandler()
                     );
    }
}

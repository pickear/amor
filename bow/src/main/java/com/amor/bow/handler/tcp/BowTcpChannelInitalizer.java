package com.amor.bow.handler.tcp;

import com.amor.bow.handler.*;
import com.amor.core.context.ConfigurableContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

/**
 * @author dylan
 * @time 2017/6/15
 */
public class BowTcpChannelInitalizer extends ChannelInitializer<SocketChannel> {

    private ConfigurableContext context;

    public BowTcpChannelInitalizer(ConfigurableContext context) {
        this.context = context;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline()
                     .addLast(
                             /* new LoggingHandler(LogLevel.INFO),*/
                             new BowChannelManagerHandler(),
                             new BowTcpProtocolFrontHandler(context)
                     );
    }
}

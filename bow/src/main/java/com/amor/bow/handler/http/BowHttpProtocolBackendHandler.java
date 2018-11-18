package com.amor.bow.handler.http;

import com.amor.common.helper.ByteHelper;
import com.amor.common.manager.ChannelManager;
import com.amor.core.protocol.HttpProtocol;
import com.amor.core.protocol.TcpProtocol;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author dylan
 * @time 2017/6/15
 */
public class BowHttpProtocolBackendHandler extends SimpleChannelInboundHandler<HttpProtocol>{

    private Logger logger = LoggerFactory.getLogger(BowHttpProtocolBackendHandler.class);

    @Override
    public void channelRead0(ChannelHandlerContext ctx, HttpProtocol protocol) throws Exception {
        Channel clientChannel = ChannelManager.get(protocol.getClientId());
        if(null == clientChannel || !clientChannel.isActive()){
            logger.error("can not get the client[{}] channel，replay fail!",protocol.getClientId());
            return;
        }
        ByteBuf byteBuf = ByteHelper.byteToByteBuf(protocol.getMsg());
        logger.debug("receive message from arrow,replay to client.msg : {},client : {}",protocol.getMsg(),clientChannel.remoteAddress());
        clientChannel.writeAndFlush(byteBuf)
                    .addListener(new ChannelFutureListener() {
                        @Override
                        public void operationComplete(ChannelFuture future) throws Exception {
                            if (future.isSuccess()) {
                                ctx.channel().read();
                            } else {
                                logger.warn("replay message to client fail,will be close the channel!");
                                future.channel().close();
                            }
                        }
                    });
    }
}

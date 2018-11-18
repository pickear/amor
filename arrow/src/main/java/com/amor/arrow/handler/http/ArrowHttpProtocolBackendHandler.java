/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.amor.arrow.handler.http;

import com.amor.common.helper.ByteHelper;
import com.amor.common.manager.ChannelManager;
import com.amor.core.protocol.HttpProtocol;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ArrowHttpProtocolBackendHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private final static Logger logger = LoggerFactory.getLogger(ArrowHttpProtocolBackendHandler.class);
    private final Channel inboundChannel;
    private HttpProtocol protocol;

    public ArrowHttpProtocolBackendHandler(HttpProtocol protocol, Channel inboundChannel) {
        this.protocol = protocol;
        this.inboundChannel = inboundChannel;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("mapping address[{}:{}]",protocol.getDevice().getMapIp(),protocol.getDevice().getMapPort());

    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, ByteBuf byteBuf) throws Exception {
        HttpProtocol protocol = new HttpProtocol();
        protocol.setClientId(this.protocol.getClientId());
        protocol.setMsg(ByteHelper.byteBufToByte(byteBuf));
        logger.info("receive http message,replay to bow'client,bow : [{}]，client : [{}]",protocol.getMsg(),inboundChannel.remoteAddress(),protocol.getClientId());
        inboundChannel.writeAndFlush(protocol).addListener(new ChannelFutureListener() {
            public void operationComplete(ChannelFuture future) {
                if (future.isSuccess()) {
                    ctx.channel().read();
                } else {
                    logger.error("replay fail!");
                }
            }
        });
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info("the mapped address[{}] be closed!",ctx.channel().remoteAddress());
        ChannelManager.closeOnFlush(ctx.channel());
    }

}

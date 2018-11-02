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
package com.amor.arrow.handler.tcp;

import com.amor.common.helper.ByteHelper;
import com.amor.common.manager.ChannelManager;
import com.amor.core.protocol.DeviceOnlineProtocol;
import com.amor.core.protocol.TcpProtocol;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ArrowTcpProtocolBackendHandler extends ChannelInboundHandlerAdapter {

    private final static Logger logger = LoggerFactory.getLogger(ArrowTcpProtocolBackendHandler.class);
    private final Channel inboundChannel;
    private DeviceOnlineProtocol protocol;

    public ArrowTcpProtocolBackendHandler(DeviceOnlineProtocol protocol, Channel inboundChannel) {
        this.protocol = protocol;
        this.inboundChannel = inboundChannel;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("与映射地址[{}:{}]连接建立!",protocol.getDevice().getMapIp(),protocol.getDevice().getMapPort());

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        TcpProtocol protocol = new TcpProtocol();
        protocol.setClientId(this.protocol.getClientId());
        ByteBuf byteBuf = (ByteBuf) msg;
        protocol.setMsg(ByteHelper.byteBufToByte(byteBuf));
        logger.debug("读取到映射地址的消息{},转发给bow[{}]的客户端[{}]",protocol.getMsg(),inboundChannel.remoteAddress(),protocol.getClientId());
        inboundChannel.writeAndFlush(protocol).addListener(new ChannelFutureListener() {
            public void operationComplete(ChannelFuture future) {
                if (future.isSuccess()) {
                    ctx.channel().read();
                } else {
                    future.channel().close();
                }
            }
        });
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
       /* logger.info("与映射地址[{}:{}]的连接已被关闭!",protocol.getBody().getMapIp(),
                protocol.getBody().getMapPort());*/

        ChannelManager.closeOnFlush(ctx.channel());
    }

}

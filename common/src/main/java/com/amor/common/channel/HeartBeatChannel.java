package com.amor.common.channel;

import com.amor.core.protocol.HeartBeatProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * Created by dylan on 2017/10/2.
 */
public abstract class HeartBeatChannel extends SimpleChannelInboundHandler<HeartBeatProtocol>{
   public static final String PING = "ping";
   public static final String PONG ="pong";

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, HeartBeatProtocol protocol) throws Exception {

        if(protocol.getMessage() == PING){
            pong(channelHandlerContext);
        }else if(protocol.getMessage() == PONG){
            System.out.println("receive pong mesage :" + channelHandlerContext.channel().remoteAddress());
        }else {
            handleData(channelHandlerContext,protocol);
        }
    }

    private void pong(ChannelHandlerContext context){
        heartBeatMsg(PONG,context);
    }

    protected void ping(ChannelHandlerContext context){
        heartBeatMsg(PING,context);
    }

    private void heartBeatMsg(String msg,ChannelHandlerContext context){
        HeartBeatProtocol heartBeatProtocol = new HeartBeatProtocol();
        heartBeatProtocol.setMessage(msg);
        context.writeAndFlush(heartBeatProtocol);
    }

    protected abstract void handleData(ChannelHandlerContext context,HeartBeatProtocol msg);

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof IdleStateEvent){
            IdleStateEvent idleStateEvent = (IdleStateEvent)evt;
            switch (idleStateEvent.state()){
                case READER_IDLE:
                    handleReaderIdle(ctx);
                    break;
                case WRITER_IDLE:
                    handleWriterIdle(ctx);
                    break;
                case ALL_IDLE:
                    handleAllIdle(ctx);
                    break;
                default:
                    break;
            }
        }
    }

    protected void handleReaderIdle(ChannelHandlerContext context){
        System.out.println("handleReaderIdle");
    }
    protected void handleWriterIdle(ChannelHandlerContext context){
        System.out.println("handleWriterIdle");
    }
    protected void handleAllIdle(ChannelHandlerContext context){
        System.out.println("handleAllIdle");
    }

}

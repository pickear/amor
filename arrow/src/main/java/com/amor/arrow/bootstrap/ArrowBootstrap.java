package com.amor.arrow.bootstrap;

import com.amor.arrow.config.ArrowProperties;
import com.amor.arrow.handler.ArrowChannelInitalizer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * @author dylan
 * @time 2017/6/15
 */
public class ArrowBootstrap {

    private final static Logger logger = LoggerFactory.getLogger(ArrowBootstrap.class);
    public boolean closedArrow = false;
    private EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    private Bootstrap bootstrap;
    private Channel channel;
    private ArrowProperties properties = ArrowProperties.instance();

    public void start(){
        try {

            bootstrap = new Bootstrap();
            bootstrap.group(bossGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ArrowChannelInitalizer());

            /*Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    bossGroup.shutdownGracefully();
                }
            });*/
            connect();
        }catch (Exception e){
            logger.error(e.getMessage());
        }/*finally {
            bossGroup.shutdownGracefully();
        }*/
    }

    /**
     *
     */
    public void connect(){

        if(null != channel && channel.isActive()){
            return;
        }
        logger.info("连接bow[{}:{}]",properties.getBowIp(),properties.getBowPort());
        ChannelFuture future = bootstrap.connect(properties.getBowIp(),properties.getBowPort());
        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if(channelFuture.isSuccess()){
                    logger.info("连接服务端成功");
                    channel = channelFuture.channel();
                }else {
                    if(!closedArrow){
                        logger.info("连接服务器失败，10秒后重连!");
                        channelFuture.channel()
                                .eventLoop()
                                .schedule(()->{
                                    connect();
                                }, 10, TimeUnit.SECONDS);
                    }
                }
            }
        });
    }

    public void closed(){
        closedArrow = true;
        bossGroup.shutdownGracefully();
    }
}

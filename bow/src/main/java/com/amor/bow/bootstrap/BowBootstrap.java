package com.amor.bow.bootstrap;

import com.amor.bow.config.BowProxyProperties;
import com.amor.bow.handler.BowChannelInitalizer;
import com.amor.bow.helper.SpringBeanHolder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 * @author dylan
 * @time 2017/6/15
 */
public class BowBootstrap {

    protected BowProxyProperties properties = SpringBeanHolder.getBean(BowProxyProperties.class);

    protected EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    protected EventLoopGroup workerGroup = new NioEventLoopGroup();
    protected ServerBootstrap bootstrap = new ServerBootstrap();

    public BowBootstrap() {
        bootstrap.group(bossGroup,workerGroup)
                .option(ChannelOption.SO_REUSEADDR,true)
                .option(ChannelOption.SO_BACKLOG,128)
                .childOption(ChannelOption.SO_KEEPALIVE,true)
                .childOption(ChannelOption.TCP_NODELAY,true)
                .channel(NioServerSocketChannel.class)
                .childHandler(new BowChannelInitalizer());
    }

    public ServerBootstrap getBootstrap() {
        return bootstrap;
    }

    public void setBootstrap(ServerBootstrap bootstrap) {
        this.bootstrap = bootstrap;
    }

    public BowProxyProperties getProperties() {
        return properties;
    }

    public void setProperties(BowProxyProperties properties) {
        this.properties = properties;
    }

    public void start(){
        try {
            bind(properties.getLocalPort()).channel()
                    .closeFuture()
                    .sync();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public void stop(){
        if(null != bossGroup && !bossGroup.isShutdown()){
            bossGroup.shutdownGracefully();
        }
        if(null != workerGroup && !workerGroup.isShutdown()){
            workerGroup.shutdownGracefully();
        }
    }

    public ChannelFuture bind(int port){
        return bootstrap.bind(new InetSocketAddress(port));
    }
}
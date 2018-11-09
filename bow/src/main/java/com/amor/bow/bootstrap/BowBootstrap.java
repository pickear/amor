package com.amor.bow.bootstrap;

import com.amor.core.config.BowConfig;
import com.amor.core.context.ConfigurableContext;
import com.amor.core.context.Context;
import com.amor.core.context.ContextHolder;
import com.amor.plugin.Plugin;
import com.amor.plugin.PluginManager;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * @author dylan
 * @time 2017/6/15
 */
public class BowBootstrap {

    protected EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    protected EventLoopGroup workerGroup = new NioEventLoopGroup();
    protected ServerBootstrap bootstrap = new ServerBootstrap();
    protected Context context = new ConfigurableContext();
    protected PluginManager pluginManager = new PluginManager();

    public BowBootstrap(ChannelHandler channelHandler) {

        ContextHolder.setContext(context);
        List<Plugin> plugins = pluginManager.getPlugins();
        for(Plugin plugin : plugins){
            plugin.before(context);
        }

        bootstrap.group(bossGroup,workerGroup)
                .option(ChannelOption.SO_REUSEADDR,true)
                .option(ChannelOption.SO_BACKLOG,128)
                .childOption(ChannelOption.SO_KEEPALIVE,true)
                .childOption(ChannelOption.TCP_NODELAY,true)
                .channel(NioServerSocketChannel.class)
                .childHandler(channelHandler);
    }

    public ServerBootstrap getBootstrap() {
        return bootstrap;
    }

    public void setBootstrap(ServerBootstrap bootstrap) {
        this.bootstrap = bootstrap;
    }

    public BowConfig getBowConfig(){
        return ((ConfigurableContext)context).getBowConfig();
    }

    public void start(final int port){
        new Thread(()->{
            try {
                bind(port).channel()
                        .closeFuture()
                        .sync();
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                bossGroup.shutdownGracefully();
                workerGroup.shutdownGracefully();
            }
        }).start();

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

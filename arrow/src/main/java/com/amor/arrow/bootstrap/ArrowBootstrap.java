package com.amor.arrow.bootstrap;

import com.amor.arrow.handler.ArrowChannelInitalizer;
import com.amor.arrow.listener.CloseListener;
import com.amor.arrow.listener.ReconectionListener;
import com.amor.core.context.ConfigurableContext;
import com.amor.core.context.Context;
import com.amor.core.context.ContextHolder;
import com.amor.core.listener.event.EventPublisher;
import com.amor.plugin.Plugin;
import com.amor.plugin.PluginManager;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
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
    private Context context = new ConfigurableContext();
    private PluginManager pluginManager = new PluginManager();
    public void start(){
        try {
            ContextHolder.setContext(context);
            List<Plugin> plugins = pluginManager.getPlugins();
            for(Plugin plugin : plugins){
                plugin.before(context);
            }
            bootstrap = new Bootstrap();
            bootstrap.group(bossGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ArrowChannelInitalizer());

            connect();
            EventPublisher.register(new ReconectionListener(this));
            EventPublisher.register(new CloseListener(this));
        }catch (Exception e){
            logger.error(e.getMessage());
        }
    }

    /**
     *
     */
    public void connect(){
        final ConfigurableContext context = (ConfigurableContext) this.context;
        if(null != channel && channel.isActive()){
            return;
        }
        logger.info("连接bow[{}:{}]",context.getArrowConfig().getBowIp(),context.getArrowConfig().getBowPort());
        ChannelFuture future = bootstrap.connect(context.getArrowConfig().getBowIp(),context.getArrowConfig().getBowPort());
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

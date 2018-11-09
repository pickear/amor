package com.amor.bow.bootstrap;

import com.amor.bow.handler.BowChannelInitalizer;
import com.amor.core.context.ConfigurableContext;
import io.netty.channel.ChannelHandler;

/**
 * @author dylan
 * @time 2017/6/15
 */
public class BowTcpBootstrap extends BowBootstrap{


    public void start(){
        super.start(getBowConfig().getTcpPort(),new BowChannelInitalizer((ConfigurableContext) context));
    }
}

package com.amor.bow.bootstrap;

import com.amor.bow.handler.http.BowHttpChannelInitalizer;
import com.amor.core.context.ConfigurableContext;

/**
 * @author dylan
 * @time 2017/6/15
 */
public class BowHttpBootstrap extends BowBootstrap{


    public void start(){
        super.start(getBowConfig().getHttpPort(),new BowHttpChannelInitalizer((ConfigurableContext) context));
    }
}

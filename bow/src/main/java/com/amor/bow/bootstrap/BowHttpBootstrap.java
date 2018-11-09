package com.amor.bow.bootstrap;

import com.amor.bow.handler.http.BowHttpChannelInitalizer;

/**
 * @author dylan
 * @time 2017/6/15
 */
public class BowHttpBootstrap extends BowBootstrap{


    public BowHttpBootstrap() {
        super(new BowHttpChannelInitalizer());
    }

    public void start(){
        super.start(getBowConfig().getHttpPort());
    }
}

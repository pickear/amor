package com.amor.bow.bootstrap;

import com.amor.bow.handler.BowChannelInitalizer;

/**
 * @author dylan
 * @time 2017/6/15
 */
public class BowTcpBootstrap extends BowBootstrap{


    public BowTcpBootstrap() {
        super(new BowChannelInitalizer());
    }

    public void start(){
        super.start(getBowConfig().getTcpPort());
    }
}

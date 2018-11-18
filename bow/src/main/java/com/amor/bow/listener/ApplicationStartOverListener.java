package com.amor.bow.listener;

import com.amor.bow.bootstrap.BowHttpBootstrap;
import com.amor.bow.bootstrap.BowTcpBootstrap;
import com.amor.core.listener.EventListener;
import com.amor.core.listener.event.ApplicationStartOverEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**应用启动完监听器
 * Created by dylan on 17-3-12.
 */
public class ApplicationStartOverListener extends EventListener<ApplicationStartOverEvent> {

    private final Logger logger = LoggerFactory.getLogger(ApplicationStartOverListener.class);

    @Override
    protected void handleEvent(ApplicationStartOverEvent event) {
        BowTcpBootstrap tcpBootstrap = new BowTcpBootstrap();
        logger.info("turn on tcp，bind ip["+tcpBootstrap.getBowConfig().getLocalIp()+"]," +
                "listen port["+tcpBootstrap.getBowConfig().getTcpPort()+"]!");
        tcpBootstrap.start();

        BowHttpBootstrap httpBootstrap = new BowHttpBootstrap();
        logger.info("turn on http，bind ip["+httpBootstrap.getBowConfig().getLocalIp()+"]," +
                "listen port["+httpBootstrap.getBowConfig().getHttpPort()+"]!");
        httpBootstrap.start();
    }

}

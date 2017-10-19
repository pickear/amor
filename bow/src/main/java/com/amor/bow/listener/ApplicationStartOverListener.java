package com.amor.bow.listener;

import com.amor.bow.bootstrap.BowBootstrap;
import com.amor.bow.bootstrap.BowHttpBootstrap;
import com.amor.bow.bootstrap.BowTcpBootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
/**应用启动完监听器
 * Created by dylan on 17-3-12.
 */
public class ApplicationStartOverListener implements ApplicationListener<ContextRefreshedEvent> {

    private final Logger logger = LoggerFactory.getLogger(ApplicationStartOverListener.class);

    public ApplicationStartOverListener() {
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        BowTcpBootstrap tcpBootstrap = event.getApplicationContext().getBean(BowTcpBootstrap.class);
        logger.info("应用启动完成，开户启动tcp，绑定ip["+tcpBootstrap.getProperties().getLocalIp()+"]," +
                "监听端口["+tcpBootstrap.getProperties().getTcpPort()+"]!");
        tcpBootstrap.start();

        BowHttpBootstrap httpBootstrap = event.getApplicationContext().getBean(BowHttpBootstrap.class);
        logger.info("应用启动完成，开户启动http，绑定ip["+httpBootstrap.getProperties().getLocalIp()+"]," +
                "监听端口["+httpBootstrap.getProperties().getHttpPort()+"]!");
        httpBootstrap.start();
    }
}

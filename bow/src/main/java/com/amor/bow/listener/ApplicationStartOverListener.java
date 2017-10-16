package com.amor.bow.listener;

import com.amor.bow.config.BowProxyProperties;
import com.amor.bow.proxy.BowProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
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
        BowProxy bowProxy = event.getApplicationContext().getBean(BowProxy.class);
        logger.info("应用启动完成，开户启动bow，绑定ip["+bowProxy.getProperties().getLocalIp()+"]," +
                "监听端口["+bowProxy.getProperties().getTcpPort()+"]!");
        bowProxy.start();
    }
}

package com.amor.arrow.listener;

import com.amor.arrow.bootstrap.ArrowBootstrap;
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

        logger.info("开始启动arrow......");
        ArrowBootstrap arrowProxy = event.getApplicationContext().getBean(ArrowBootstrap.class);
        arrowProxy.start();
    }
}

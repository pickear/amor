package com.amor.arrow.listener;

import com.amor.arrow.bootstrap.ArrowBootstrap;
import com.amor.core.listener.EventListener;
import com.amor.core.listener.event.ApplicationStartOverEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**应用启动完监听器
 * Created by dylan on 17-3-12.
 */
public class ReconectionListener extends EventListener<ApplicationStartOverEvent> {

    private final Logger logger = LoggerFactory.getLogger(ReconectionListener.class);
    private ArrowBootstrap bootstrap;
    public ReconectionListener(ArrowBootstrap bootstrap) {
        this.bootstrap = bootstrap;
    }

    @Override
    protected void handleEvent(ApplicationStartOverEvent event) {
        logger.info("reconnect bow now");
        bootstrap.connect();
    }
}

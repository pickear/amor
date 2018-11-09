package com.amor.arrow.listener;

import com.amor.arrow.bootstrap.ArrowBootstrap;
import com.amor.arrow.listener.event.CloseEvent;
import com.amor.core.listener.EventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author dylan
 * @date 2018/11/9
 */
public class CloseListener extends EventListener<CloseEvent> {

    private final Logger logger = LoggerFactory.getLogger(CloseListener.class);
    private ArrowBootstrap bootstrap;

    public CloseListener(ArrowBootstrap bootstrap) {
        this.bootstrap = bootstrap;
    }
    @Override
    protected void handleEvent(CloseEvent event) {
        logger.info("关闭与bow的连接......");
        bootstrap.closed();
    }
}

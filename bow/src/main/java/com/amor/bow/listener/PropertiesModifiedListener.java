package com.amor.bow.listener;

import com.amor.bow.helper.SpringBeanHolder;
import com.amor.bow.listener.event.PropertiesModifiedEvent;
import com.amor.bow.repository.impl.UserLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;

/**
 * Created by dell on 2017/10/25.
 */
public class PropertiesModifiedListener implements ApplicationListener<PropertiesModifiedEvent> {

    private final static Logger logger = LoggerFactory.getLogger(PropertiesModifiedListener.class);

    @Override
    public void onApplicationEvent(PropertiesModifiedEvent event) {
        logger.info("listen properties modified event,reload it!");
        UserLoader loader = SpringBeanHolder.getBean(UserLoader.class);
        loader.reload();
    }
}

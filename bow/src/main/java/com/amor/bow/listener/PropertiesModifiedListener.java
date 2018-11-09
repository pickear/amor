package com.amor.bow.listener;

import com.amor.core.listener.EventListener;
import com.amor.core.listener.event.PropertiesModifiedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by dell on 2017/10/25.
 */
public class PropertiesModifiedListener extends EventListener<PropertiesModifiedEvent> {

    private final static Logger logger = LoggerFactory.getLogger(PropertiesModifiedListener.class);

    @Override
    public void handleEvent(PropertiesModifiedEvent event) {
        logger.info("listen properties modified event,reload it!");
        throw new RuntimeException("不能调用该方法");
        /*UserLoader loader = new UserLoader();
        loader.reload();*/
    }

}

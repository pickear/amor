package com.amor.bow.listener;

import com.amor.bow.config.BowProxyProperties;
import com.amor.bow.helper.SpringBeanHolder;
import com.amor.bow.listener.event.PropertiesModifiedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.bind.PropertiesConfigurationFactory;
import org.springframework.boot.context.config.ConfigFileApplicationListener;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.validation.BindException;

import java.util.List;

/**
 * Created by dell on 2017/10/25.
 */
public class PropertiesModifiedListener extends ConfigFileApplicationListener{

    private final static Logger logger = LoggerFactory.getLogger(PropertiesModifiedListener.class);

    private ApplicationEnvironmentPreparedEvent environmentPreparedEvent = null;
    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if(event instanceof ApplicationEnvironmentPreparedEvent){
            this.environmentPreparedEvent = (ApplicationEnvironmentPreparedEvent)event;
        }
        if(event instanceof PropertiesModifiedEvent){
            logger.info("listen a prpperties modified event,reload the properties.");
            AutowireCapableBeanFactory beanFactory = SpringBeanHolder.getContext()
                    .getAutowireCapableBeanFactory();

            BowProxyProperties properties = SpringBeanHolder.getBean(BowProxyProperties.class);
            if(null != properties){
                beanFactory.destroyBean(properties);
            }
            postProcessEnvironment(environmentPreparedEvent.getEnvironment(),environmentPreparedEvent.getSpringApplication());
            beanFactory.createBean(BowProxyProperties.class);
            /*List<PropertiesConfigurationFactory> propertiesConfigurationFactories =  SpringFactoriesLoader.
                    loadFactories(PropertiesConfigurationFactory.class,getClass().getClassLoader());
            propertiesConfigurationFactories.forEach(factory ->{
                try {
                    factory.bindPropertiesToTarget();
                } catch (BindException e) {
                    e.printStackTrace();
                }
            });*/
        }

    }
}

package com.amor.bow.config;

import com.amor.bow.proxy.BowProxy;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by dylan on 2017/9/30.
 */
@Configuration
public class ApplicationBeanConfiguration {

    @Bean
    public BowProxy createBowProxy(){
        return new BowProxy();
    }
}

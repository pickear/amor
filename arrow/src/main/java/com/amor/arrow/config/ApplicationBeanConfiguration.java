package com.amor.arrow.config;

import com.amor.arrow.proxy.ArrowProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by dylan on 2017/9/30.
 */
@Configuration
public class ApplicationBeanConfiguration {

    @Bean
    public ArrowProxy createArrowProxy(){
        return new ArrowProxy();
    }
}

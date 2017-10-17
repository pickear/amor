package com.amor.arrow.config;

import com.amor.arrow.bootstrap.ArrowBootstrap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by dylan on 2017/9/30.
 */
@Configuration
public class ApplicationBeanConfiguration {

    @Bean
    public ArrowBootstrap createArrowProxy(){
        return new ArrowBootstrap();
    }
}

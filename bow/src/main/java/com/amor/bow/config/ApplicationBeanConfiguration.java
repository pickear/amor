package com.amor.bow.config;

import com.amor.bow.bootstrap.BowBootstrap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by dylan on 2017/9/30.
 */
@Configuration
public class ApplicationBeanConfiguration {

    @Bean
    public BowBootstrap createBowBootstrap(){
        return new BowBootstrap();
    }

}

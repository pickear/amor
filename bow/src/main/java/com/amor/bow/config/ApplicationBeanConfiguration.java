package com.amor.bow.config;

import com.amor.bow.bootstrap.BowHttpBootstrap;
import com.amor.bow.bootstrap.BowTcpBootstrap;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by dylan on 2017/9/30.
 */
@Configuration
@EnableConfigurationProperties({BowProxyProperties.class})
public class ApplicationBeanConfiguration {

    @Bean
    public BowTcpBootstrap createBowTcpBootstrap(){
        return new BowTcpBootstrap();
    }

    @Bean
    public BowHttpBootstrap createBowHttpBootstrap(){
        return new BowHttpBootstrap();
    }

}

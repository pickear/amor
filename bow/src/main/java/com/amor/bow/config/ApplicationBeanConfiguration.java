package com.amor.bow.config;

import com.amor.bow.bootstrap.BowBootstrap;
import com.amor.bow.bootstrap.BowHttpBootstrap;
import com.amor.bow.bootstrap.BowTcpBootstrap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by dylan on 2017/9/30.
 */
@Configuration
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

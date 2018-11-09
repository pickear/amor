package com.amor.core.context;

import com.amor.core.config.ArrowConfig;
import com.amor.core.config.BowConfig;

/**
 * @author dylan
 * @date 2018/11/9
 */
public class ConfigurableContext extends Context {


    private ArrowConfig arrowConfig;
    private BowConfig bowConfig;

    public ArrowConfig getArrowConfig() {
        return arrowConfig;
    }

    public void setArrowConfig(ArrowConfig arrowConfig) {
        this.arrowConfig = arrowConfig;
    }

    public BowConfig getBowConfig() {
        return bowConfig;
    }

    public void setBowConfig(BowConfig bowConfig) {
        this.bowConfig = bowConfig;
    }
}

package com.amor.plugin.bow.config;

import com.amor.core.context.ConfigurableContext;
import com.amor.core.context.Context;
import com.amor.plugin.Plugin;

/**
 * @author dylan
 * @date 2018/11/2
 */
public class ConfigurationPlugin implements Plugin {

    @Override
    public void before(Context context) {
        if(context instanceof ConfigurableContext){
            ((ConfigurableContext)context).setBowConfig(BowConfiguration.instance());
        }
        System.out.println("memory plugin before");
    }

    @Override
    public void after() {
        System.out.println("memory plugin after");
    }
}

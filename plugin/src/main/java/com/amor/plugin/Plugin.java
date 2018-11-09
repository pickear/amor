package com.amor.plugin;

import com.amor.core.context.Context;

/**
 * @author dylan
 * @date 2018/11/2
 */
public interface Plugin {

    void before(Context context);

    void after();
}

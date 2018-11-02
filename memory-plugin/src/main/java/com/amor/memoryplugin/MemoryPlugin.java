package com.amor.memoryplugin;

import com.amor.plugin.Plugin;

/**
 * @author dylan
 * @date 2018/11/2
 */
public class MemoryPlugin implements Plugin {

    @Override
    public void before() {
        System.out.println("memory plugin before");
    }

    @Override
    public void after() {
        System.out.println("memory plugin after");
    }
}

package com.amor.plugin;

/**
 * @author dylan
 * @date 2018/11/2
 */
public class PluginManagerTest {

    public static void main(String[] args) {
        PluginManager pluginManager = new PluginManager();
        pluginManager.loadPluginsIntoClassLoader();
    }
}

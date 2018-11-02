package com.amor.plugin;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author dylan
 * @date 2018/11/2
 */
public class PluginManager {

    private final static String PLUGIN_PATH = "plugins";
    private final static String PROPERTIES_FILE = "plugin.properties";

    List<Plugin> pugins = new LinkedList<>();

    public void loadPluginsIntoClassLoader(){

        try {
            List<Path> pluginsPath = Files.list(Paths.get("D:\\","plugins")).collect(Collectors.toList());
            if(Objects.nonNull(pluginsPath) && !pluginsPath.isEmpty()){
                for(Path pluginPath : pluginsPath){
                    List<Path> pluginFiles = Files.list(pluginPath).collect(Collectors.toList());
                    for(Path pluginFile : pluginFiles){
                        if(StringUtils.endsWith(pluginFile.toFile().getName(),".jar") ||
                                StringUtils.endsWith(pluginFile.toFile().getName(),"zip")){

                            loadJar(pluginFile);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadJar(Path jarPath){
        try {
            Method method = URLClassLoader.class.getDeclaredMethod("addURL",URL.class);
            try {
                method.setAccessible(true);
                method.invoke(getURLClassLoader(),jarPath.toUri().toURL());
            } catch (IllegalAccessException |InvocationTargetException | MalformedURLException e) {
                e.printStackTrace();
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

    }

    private URLClassLoader getURLClassLoader(){
        return (URLClassLoader) ClassLoader.getSystemClassLoader();
    }
}

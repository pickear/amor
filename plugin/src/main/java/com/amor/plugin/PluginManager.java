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
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author dylan
 * @date 2018/11/2
 */
public class PluginManager {

    private final static String PLUGIN_PATH = "plugins";
    private final static String PROPERTIES_FILE = "plugin.properties";

    List<Plugin> plugins = new LinkedList<>();

    public void loadPlugins(){

        try {
            List<Path> pluginsPath = Files.list(Paths.get("D:\\",PLUGIN_PATH)).collect(Collectors.toList());
            loadPluginsIntoClassLoader(pluginsPath);

            Set<String> pluginClassNames = getPluginClassNames(pluginsPath);
            for(String pluginClassName : pluginClassNames){
                Plugin plugin = (Plugin) Class.forName(pluginClassName).newInstance();
                plugins.add(plugin);
            }
        } catch (IOException | ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }

    }

    /**
     *
     * @param pluginsPath
     * @return
     * @throws IOException
     */
    private Set<String> getPluginClassNames(List<Path> pluginsPath) throws IOException {
        Set<String> pluginClassNames = new HashSet<>();
        if(Objects.nonNull(pluginsPath) && !pluginsPath.isEmpty()){
            for(Path pluginPath : pluginsPath){
                List<Path> pluginFiles = Files.list(pluginPath).collect(Collectors.toList());
                for(Path pluginFile : pluginFiles){
                    if(StringUtils.equals(pluginFile.toFile().getName(),PROPERTIES_FILE)){
                        pluginClassNames.add("com.amor.memoryplugin.MemoryPlugin");
                    }
                }
            }
        }
        return pluginClassNames;
    }


    /**
     *
     * @param pluginsPath
     * @throws IOException
     */
    public void loadPluginsIntoClassLoader(List<Path> pluginsPath) throws IOException {
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
    }

    /**
     *
     * @param jarPath
     */
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

    /**
     *
     * @return
     */
    private URLClassLoader getURLClassLoader(){
        return (URLClassLoader) ClassLoader.getSystemClassLoader();
    }
}

package com.amor.plugin;

import com.amor.core.helper.ClassPathResourceHelper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.*;
import java.nio.charset.Charset;
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

    private final static Logger logger = LoggerFactory.getLogger(PluginManager.class);
    public final static String PLUGIN_PATH = "plugins";
    public final static String PROPERTIES_FILE = "plugin.properties";

    List<Plugin> plugins = new LinkedList<>();

    public PluginManager() {
        loadPlugins();
    }

    public PluginManager(String pluginPath) {
        loadPlugins(pluginPath);
    }

    public List<Plugin> getPlugins(){
        return plugins;
    }

    public void loadPlugins(){
        loadPlugins(PluginPathHelper.getPluginPah(this.getClass()));
    }

    public void loadPlugins(String pluginPath){
        try {
            logger.info("load plugins : " + pluginPath);
            List<Path> pluginsPath = Files.list(Paths.get(pluginPath)).collect(Collectors.toList());
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
                        String classNameLines = Files.readAllLines(pluginFile,Charset.forName("utf-8")).get(0);
                        pluginClassNames.add(StringUtils.split(classNameLines,"=")[1]);
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

                        logger.info("load plugin jar : " + pluginFile);
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

package com.amor.plugin;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public final class PluginPathHelper {


    /**
     *
     * @param clazz
     * @return
     */
    public static String getJarPath(Class clazz){
        return clazz.getProtectionDomain().getCodeSource().getLocation().getPath();
    }

    /**
     *
     * @param clazz
     * @return
     */
    public static String getPluginPah(Class clazz){

        String jarPath = getJarPath(clazz);
        int firstIndex = jarPath.lastIndexOf(System.getProperty("path.separator")) + 1;
        int lastIndex = jarPath.lastIndexOf(File.separator) + 1;
        String path = jarPath.substring(firstIndex, lastIndex);
        if(StringUtils.endsWith(path,"lib/")){
            path = StringUtils.removeEnd(path,"lib/");
        }
        try {
            return URLDecoder.decode(path + PluginManager.PLUGIN_PATH, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     */
    private PluginPathHelper(){}
}

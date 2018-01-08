package com.amor.common.helper;

import java.io.InputStream;

/**
 * Created by dell on 2018/1/8.
 */
public final class ClassPathResourceHelper {

    /**
     *
     */
    private static ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

    /**
     *
     * @param path
     * @return
     */
    public static InputStream getInputStream(String path){
        return classLoader.getResourceAsStream(path);
    }
}

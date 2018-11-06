package com.amor.core.helper;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

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

    /**
     *
     * @param path
     * @return
     * @throws FileNotFoundException
     */
    public static OutputStream getOutputStream(String path) throws FileNotFoundException {
        return new FileOutputStream(classLoader.getResource(path).getFile());
    }

    /**
     *
     * @param path
     * @return
     */
    public static URL getURL(String path){
        return classLoader.getResource(path);
    }

    /**
     *
     * @param path
     * @return
     * @throws URISyntaxException
     */
    public static URI getUri(String path) throws URISyntaxException {
        return getURL(path).toURI();
    }
}

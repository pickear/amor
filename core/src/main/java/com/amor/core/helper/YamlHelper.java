package com.amor.core.helper;

import org.yaml.snakeyaml.Yaml;

import java.io.*;

/**
 * Created by dylan on 2017/10/26.
 */
public final class YamlHelper {

    private final static Yaml yaml = new Yaml();

    /**
     *
     * @param yamlFile
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T load(InputStream yamlFile,Class<T> clazz){
        return yaml.loadAs(yamlFile,clazz);
    }

    /**
     *
     * @param yamlFile
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T load(Reader yamlFile,Class<T> clazz){

        return yaml.loadAs(yamlFile,clazz);
    }

    /**
     *
     * @param yamlFile
     * @param clazz
     * @param <T>
     * @return
     * @throws FileNotFoundException
     */
    public static <T> T locadPath(String yamlFile,Class<T> clazz) throws FileNotFoundException {
        return load(new FileInputStream(new File(yamlFile)),clazz);
    }

    /**
     *
     * @param data
     * @param path
     * @throws IOException
     */
    public static void dump(Object data,String path) throws IOException {
        yaml.dump(data,new OutputStreamWriter(ClassPathResourceHelper.getOutputStream(path)));
    }
    private YamlHelper(){}
}

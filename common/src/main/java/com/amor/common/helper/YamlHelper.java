package com.amor.common.helper;

import org.yaml.snakeyaml.Yaml;

import java.io.*;

/**
 * Created by dylan on 2017/10/26.
 */
public final class YamlHelper {

    private final static Yaml yaml = new Yaml();

    public static <T> T load(InputStream yamlFile,Class<T> clazz){
        return yaml.loadAs(yamlFile,clazz);
    }

    public static <T> T load(Reader yamlFile,Class<T> clazz){

        return yaml.loadAs(yamlFile,clazz);
    }

    public static <T> T locadPath(String yamlFile,Class<T> clazz) throws FileNotFoundException {
        return load(new FileInputStream(new File(yamlFile)),clazz);
    }
    private YamlHelper(){}
}

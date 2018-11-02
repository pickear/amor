package com.amor.core.helper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by dylan on 2017/10/3.
 */
public final class GsonHelper {

    private final static Gson gson = new GsonBuilder().create();

    /**
     * @param obj
     * @return
     */
    public static String toJson(Object obj){
        return gson.toJson(obj);
    }

    /**
     * @param json
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T fromJson(String json,Class<T> clazz){
        return gson.fromJson(json,clazz);
    }
    private GsonHelper(){}
}

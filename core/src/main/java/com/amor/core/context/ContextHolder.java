package com.amor.core.context;

/**
 * @author dylan
 * @date 2018/11/9
 */
public class ContextHolder {

    private static Context context;

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        ContextHolder.context = context;
    }
}

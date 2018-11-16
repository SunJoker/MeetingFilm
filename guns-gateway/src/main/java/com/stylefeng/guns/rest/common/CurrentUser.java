package com.stylefeng.guns.rest.common;


/**
 * @Auther gongfukang
 * @Date 11/9 15:21
 */
public class CurrentUser {

    /*
        线程绑定存储空间
        InheritableThreadLocal 在线程切换时，可以绑定用户，ThreadLocal 不可以
    * */
    private static final InheritableThreadLocal<String> threadLocal = new InheritableThreadLocal<>();

    public static void saveUserId(String userId) {
        threadLocal.set(userId);
    }

    public static String getCurrentUser() {
        return threadLocal.get();
    }
}

package com.yubin.jvm.classloader;

/**
 * 类加载器的父加载器演示
 *
 * @author YUBIN
 * @create 2020-10-14
 */
public class T004_ClassLoaderLevel {

    public static void main(String[] args) {
        // App类加载器
        System.out.println(T004_ClassLoaderLevel.class.getClassLoader());
        // Bootstrap类加载器即输出null值
        System.out.println(T004_ClassLoaderLevel.class.getClassLoader().getClass().getClassLoader());
        // Extension类加载器
        System.out.println(T004_ClassLoaderLevel.class.getClassLoader().getParent());
        // Bootstrap类加载器
        System.out.println(T004_ClassLoaderLevel.class.getClassLoader().getParent().getParent());
        // 空指针异常
        System.out.println(T004_ClassLoaderLevel.class.getClassLoader().getParent().getParent().getParent());
    }
}

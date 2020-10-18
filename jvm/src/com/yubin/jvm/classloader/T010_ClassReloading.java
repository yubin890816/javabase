package com.yubin.jvm.classloader;

/**
 * 演示class的热加载
 *
 * @author YUBIN
 * @create 2020-10-17
 */
public class T010_ClassReloading {

    public static void main(String[] args) throws ClassNotFoundException {
        T006_MyClassLoader classLoader = new T006_MyClassLoader();
        Class<?> clazz1 = classLoader.loadClass("com.yubin.jvm.classloader.Hello");

        //classLoader = null;
        System.out.println(clazz1.hashCode());

        classLoader = new T006_MyClassLoader();
        Class<?> clazz2 = classLoader.loadClass("com.yubin.jvm.classloader.Hello");
        System.out.println(clazz2.hashCode());

        System.out.println(clazz1 == clazz2);
    }
}

package com.yubin.jvm.classloader;

/**
 * 类加载器的范围演示
 *
 * @author YUBIN
 * @create 2020-10-14
 */
public class T003_ClassLoaderLevel {
    public static void main(String[] args) {
        String bootPath = System.getProperty("sun.boot.class.path");
        System.out.println(bootPath.replaceAll(";", System.lineSeparator()));

        System.out.println("---------------------------");
        String extPath = System.getProperty("java.ext.dirs");
        System.out.println(extPath.replaceAll(";", System.lineSeparator()));

        System.out.println("---------------------------");
        String appPath = System.getProperty("java.class.path");
        System.out.println(appPath.replaceAll(";", System.lineSeparator()));
    }
}

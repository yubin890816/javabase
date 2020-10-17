package com.yubin.jvm.classloader;

/**
 * 演示如何将一个类加载到内存
 *
 * @author YUBIN
 * @create 2020-10-14
 */
public class T005_LoadClassByHand {

    public static void main(String[] args) throws ClassNotFoundException {
        Class<?> clazz = T005_LoadClassByHand.class.getClassLoader().loadClass("com.yubin.jvm.classloader.T002_ClassLoaderLevel");
        System.out.println(clazz.getName());

        // 利用类加载器加载资源,参考坦克图片的加载
        //T005_LoadClassByHand.class.getClassLoader().getResourceAsStream("");
    }
}

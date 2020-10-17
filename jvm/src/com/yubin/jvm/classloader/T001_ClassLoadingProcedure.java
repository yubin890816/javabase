package com.yubin.jvm.classloader;

/**
 * @author YUBIN
 * @create 2020-10-14
 */
public class T001_ClassLoadingProcedure {

    public static void main(String[] args) {

    }
}

class T {

    private static int count = 2;

    private static T t = new T();

    private T() {
        count++;
    }
}

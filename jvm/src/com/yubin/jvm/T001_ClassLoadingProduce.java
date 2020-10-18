package com.yubin.jvm;

/**
 * 演示类加载的过程
 *
 * @author YUBIN
 * @create 2020-10-17
 */
public class T001_ClassLoadingProduce {
    public static void main(String[] args) {
        System.out.println(T.count);
    }
}

class T {
    public static T t = new T();

    public static int count = 2; // 类加载时赋的默认值是0

    //public static T t = new T(); // 类加载时赋的默认值是null

    public T() {
        count++;
        System.out.println("====" + count); // t在count的前面时输出1, t在count的后面时输出3
    }
}
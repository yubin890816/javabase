package com.yubin.jvm.classloader;

/**
 * 编译器的演示
 *
 * @author YUBIN
 * @create 2020-10-16
 */
public class T009_WayToRun {

    public static void main(String[] args) {
        for (int i = 0; i < 10_0000; i++) {
            m();
        }

        long start = System.currentTimeMillis();
        for (int i = 0; i < 10_0000; i++) {
            m();
        }
        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }

    public static void m() {
        for (int i = 0; i < 10_0000; i++) {
            long j = i % 3;
        }
    }
}

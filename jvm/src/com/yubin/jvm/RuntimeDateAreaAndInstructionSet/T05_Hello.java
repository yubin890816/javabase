package com.yubin.jvm.RuntimeDateAreaAndInstructionSet;

/**
 * 演示指令集
 *
 * @author YUBIN
 * @create 2020-10-24
 */
public class T05_Hello {

    public static void main(String[] args) {
        T05_Hello h = new T05_Hello();
        int i = h.m(3);
        System.out.println(i);
    }

    public int m(int n) {
        if (n == 1) {
            return 1;
        }
        return n * m(n - 1);
    }
}

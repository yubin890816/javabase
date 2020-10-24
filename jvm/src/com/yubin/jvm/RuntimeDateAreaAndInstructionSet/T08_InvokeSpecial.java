package com.yubin.jvm.RuntimeDateAreaAndInstructionSet;

/**
 * invokespecial指令演示
 *
 * @author YUBIN
 * @create 2020-10-24
 */
public class T08_InvokeSpecial {
    public static void main(String[] args) {
        T08_InvokeSpecial t = new T08_InvokeSpecial();
        t.m(); // 非invokespecial方法
        t.n();
    }

    public final void m() {

    }

    private void n() {

    }
}

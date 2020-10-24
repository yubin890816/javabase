package com.yubin.jvm.RuntimeDateAreaAndInstructionSet;

/**
 * invokedynamic指令演示
 *
 * @author YUBIN
 * @create 2020-10-24
 */
public class T10_InvokeDynamic {

    public static void main(String[] args) {
        I i1 = C::n;
        I i2 = C::n;
        I i3 = C::n;
        System.out.println(i1.getClass());
        System.out.println(i2.getClass());
        System.out.println(i3.getClass());
    }

    public interface I {
        void m();
    }

    public static class C {
        static void n() {
            System.out.println("hello");
        }
    }
}

package com.yubin.jvm.classloader;

/**
 * 测试懒加载
 *
 * @author YUBIN
 * @create 2020-10-16
 */
public class T008_LazyLoading {

    public static void main(String[] args) throws ClassNotFoundException {
        //P p;
        //X x = new X();
        //System.out.println(P.i);
        //System.out.println(P.j);
        Class.forName("com.yubin.jvm.classloader.T008_LazyLoading$P");
    }

    public static class P {
        final static int i = 8;

        static int j = 9;

        static {
            System.out.println("p");
        }
    }

    public static class X extends P {
        static {
            System.out.println("X");
        }
    }
}

package com.yubin.lambda.functionref;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 实例方法引用 案例演示
 *  如果函数式接口的实现恰好可以通过调用一个实例的方法来实现，那么就可以使用实例方法引用。
 *
 * @author YUBIN
 * @create 2020-09-05
 */
public class Test3 {

    public String put() {
        return "put...";
    }

    public void getSize(String str) {
        System.out.println(str.length());
    }

    private String toUpperCase(String str) {
        return str.toUpperCase();
    }

    public static void main(String[] args) {
        Test3 test3 = new Test3();
        System.out.println(test3.put());

        System.out.println("函数式接口Supplier:代表一个输出");
        Supplier<String> s1 = () -> test3.put();
        Supplier<String> s2 = test3::put;
        System.out.println(s1.get());
        System.out.println(s2.get());

        System.out.println("函数式接口Consumer:代表一个输入");
        Consumer<String> c1 = (str) -> System.out.println(str.length());
        Consumer<String> c2 = test3::getSize;
        c1.accept("abc");
        c2.accept("abc");

        System.out.println("函数式接口Function:代表一个输入一个输出");
        Function<String, String> f1 = (str) -> str.toUpperCase();
        Function<String, String> f2 = test3::toUpperCase;
        System.out.println(f1.apply("abc"));
        System.out.println(f2.apply("abc"));
    }

}

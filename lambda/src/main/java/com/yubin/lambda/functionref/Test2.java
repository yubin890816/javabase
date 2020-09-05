package com.yubin.lambda.functionref;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 静态方法引用案例演示
 *  如果函数式接口的实现恰好可以通过调用一个静态方法来实现，那么就可以使用静态方法引用。
 * @author YUBIN
 * @create 2020-09-05
 */
public class Test2 {

    public static String method() {
        System.out.println("function reference......");
        return "function reference";
    }

    public static void main(String[] args) {
        // 演示一下调用method方法的效果
        //System.out.println(method());

        System.out.println("使用Lambda表达式的方式 Supplier:代表一个输出");
        Supplier<String> s1 = () -> Test2.method();
        System.out.println(s1.get());

        System.out.println("使用Lambda表达式结合方法引用的方式书写调用静态方法");
        Supplier<String> s2 = Test2::method;
        System.out.println(s2.get());

        System.out.println("使用Lambda表达式结合方法引用的方式书写调用静态方法");
        Supplier<String> s3 = Fun::staticMethod;
        System.out.println(s3.get());

        System.out.println("函数式接口Consumer:代表一个输入");
        Consumer<Integer> c1 = Fun::getSize;
        c1.accept(123);

        System.out.println("函数式接口Function:代表一个输入一个输出(一般输入和输出的类型不相同)");
        Function<String, String> f1 = (str) -> str.toUpperCase();
        Function<String, String> f2 = Fun::toUpperCase;
        System.out.println(f1.apply("abc"));
        System.out.println(f2.apply("abc"));

        System.out.println("函数式接口BiFunction:代表两个输入,一个输出");
        BiFunction<String, String, Integer> bf1 = (str1, str2) -> str1.length() + str2.length();
        BiFunction<String, String, Integer> bf2 = Fun::getLength;
        System.out.println(bf1.apply("abc", "de"));
        System.out.println(bf2.apply("abc", "de"));
    }
}

class Fun {
    public static String staticMethod() {
        return "staticMethod";
    }

    public static void getSize(int size) {
        System.out.println(size);
    }

    public static String toUpperCase(String str) {
        return str.toUpperCase();
    }

    public static Integer getLength(String str1, String str2) {
        return str1.length() + str2.length();
    }
}
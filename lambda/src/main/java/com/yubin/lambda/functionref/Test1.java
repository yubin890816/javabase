package com.yubin.lambda.functionref;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 方法引用测试案例一：非方法引用的写法
 *
 * @author YUBIN
 * @create 2020-09-04
 */
public class Test1 {

    public static void main(String[] args) {
        Function<String, Integer> f1 = (str) -> {
            return str.length();
        };
        System.out.println(f1.apply("abc"));

        Consumer<String> c = (str) -> System.out.println(str);
        c.accept("str");
    }
}

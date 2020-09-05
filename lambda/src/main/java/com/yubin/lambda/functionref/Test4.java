package com.yubin.lambda.functionref;

import java.util.function.Consumer;

/**
 * 对象方法引用案例演示
 *      抽象方法的第一个参数类型刚好是实例方法的类型，抽象方法剩余的参数恰好可以当作实例方法的参数。
 *      如果函数式接口的实例能由上面说的实例方法调用来实现的话，那么就可以使用对象方法引用。
 * @author YUBIN
 * @create 2020-09-05
 */
public class Test4 {

    public static void main(String[] args) {
        Consumer<Too> c1 = (too) -> new Too().foo();
        Consumer<Too> c2 = Too::foo;
        c1.accept(new Too());
        c2.accept(new Too());
    }

    private static class Too {
        public void foo() {
            System.out.println("Too foo...");
        }
    }
}


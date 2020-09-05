package com.yubin.lambda.functionref;

import java.util.function.Supplier;

/**
 * 构造方法引用 案例演示
 *  如果函数式接口的实现恰好可以通过调用一个类的构造方法来实现，那么就可以使用构造方法引用。
 * @author YUBIN
 * @create 2020-09-05
 */
public class Test5 {

    public static void main(String[] args) {
        Supplier<Person> s1 = () -> new Person();
        Supplier<Person> s2 = Person::new;
        s1.get();
        s2.get();
    }

    private static class Person {
        public Person() {
            System.out.println("Person 无参构造函数");
        }
    }

}

package com.yubin.lambda;

import com.yubin.lambda.why.Student;

import java.util.concurrent.Callable;

/**
 * Lambda表达式写法测试类
 *
 * @author YUBIN
 * @create 2020-09-04
 */
public class LambdaTest {

    public static void main(String[] args) throws Exception {
        Callable callable1 = new Callable() {
            @Override
            public Object call() throws Exception {
                return "非Lambda表达式写法";
            }
        };
        System.out.println(callable1.call());

        Callable callable2 = () -> {return "Lambda表达式写法1";};
        System.out.println(callable2.call());

        Callable callable3 = () -> "Lambda表达式写法2";
        System.out.println(callable3.call());

        System.out.println("==============================");

        // 自定义函数式接口
        Student entity = new Student("张三", 18, 85);
        MyFunctionalInterface myFunctionalInterface1 = new MyFunctionalInterface() {
            @Override
            public void insert(Student student) {
                System.out.println("非Lambda表达式方式插入学生," + student);
            }
        };
        myFunctionalInterface1.insert(entity);

        MyFunctionalInterface myFunctionalInterface2 = (Student student) -> {
            System.out.println("第一种Lambda表达式方式插入学生," + student);
        };
        myFunctionalInterface2.insert(entity);

        MyFunctionalInterface myFunctionalInterface3 = (student) -> {
            System.out.println("第二种Lambda表达式方式插入学生," + student);
        };
        myFunctionalInterface3.insert(entity);

        MyFunctionalInterface myFunctionalInterface4 = (student) -> System.out.println("第三种Lambda表达式方式插入学生," + student);
        myFunctionalInterface4.insert(entity);

        MyFunctionalInterface myFunctionalInterface5 = student -> print(student);
        myFunctionalInterface5.insert(entity);
    }

    private static void print(Student student) {
        System.out.println("第四种Lambda表达式方式插入学生," + student);
    }
}

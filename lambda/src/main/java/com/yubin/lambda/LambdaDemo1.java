package com.yubin.lambda;

/**
 * Lambda表达式测试类
 *
 * @author YUBIN
 * @create 2020-09-03
 */
public class LambdaDemo1 {

    public static void main(String[] args) {
        // 非Lambda表达式方式创建线程对象
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("非Lambda表达式方式创建线程对象");
            }
        }).start();

        // Lambda表达式方式创建线程对象
        new Thread(() -> {
            System.out.println("Lambda表达式方式创建线程对象");
        }).start();
    }
}

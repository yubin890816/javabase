package com.yubin.javabase.exception;

import java.util.Scanner;

/**
 * 异常测试类
 *
 * @author YUBIN
 * @create 2020-08-29
 */
public class TestException {

    /**
     * 这段程序可能出现的异常有:
     *  1、当输入的除数为0的时候
     *  2、当输入的是字符串的时候
     *  等等
     *
     *  那么应该如何解决这类问题呢?
     *  1、尝试通过if-else来解决异常问题
     *    弊端:
     *      代码臃肿
     *      程序员要花很大精力"堵漏洞"
     *      程序员很难堵住所有漏洞
     *
     *  因此,推荐大家使用异常机制来处理程序运行过程中出现的问题
     *
     * @param args
     */
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.print("请输入被除数:");
        int num1 = 0;
        if (in.hasNextInt()) {
            num1 = in.nextInt();
        } else {
            System.err.println("输入的除数不是整数,程序退出。");
            System.exit(1);
        }
        System.out.print("请输入除数:");
        int num2 = 0;
        if (in.hasNextInt()) {
            num2 = in.nextInt();
            if (0 == num2) {
                System.err.println("输入的初始是0,程序退出。");
                System.exit(1);
            }
        } else {
            System.err.println("输入的除数不是整数,程序退出。");
            System.exit(1);
        }
        System.out.println(String.format("%d / %d = %d", num1, num2, num1/ num2));
        System.out.println("感谢使用本程序！");
    }
}

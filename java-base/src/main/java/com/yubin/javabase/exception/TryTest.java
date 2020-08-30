package com.yubin.javabase.exception;

/**
 * 存在return的try-catch-finally
 */
public class TryTest {
    public static void main(String[] args) {
        //System.out.println(test1());
        System.out.println(test2());
    }

    private static int test2() {
        try {
            int i = 1 / 0;
        } catch (Exception ex) {
            System.exit(1);
            return 10;
        } finally {
            System.out.println("finally代码块");
        }

        return 0;
    }

    private static int test1() {
        int num = 10;
        try {
            System.out.println("try");
            return num += 80;
        } catch (Exception e) {
            System.out.println("error");
        } finally {
            if (num > 20) {
                System.out.println("num>20 : " + num);
            }
            System.out.println("finally");
        }
        return num;
    }
}
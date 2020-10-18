package com.yubin.jvm.jmm;

/**
 * CPU乱序演示(该段程序摘自美团)
 *
 * @author YUBIN
 * @create 2020-10-18
 */
public class T01_DisOrder {

    private static int x=0, y = 0;
    private static int a=0, b = 0;

    public static void main(String[] args) throws InterruptedException {
        int i = 0;
        for (; ; ) {
            i++;
            x = 0; y = 0;
            a = 0; b = 0;
            Thread one = new Thread(() -> {
                // 由于线程one先启动, 下面这句话让它等一等线程two, 读者可根据自己电脑的实际性能适当调整等待时间
                //shortWait(100000);
                a = 1;
                x = b;
            });

            Thread other = new Thread(() -> {
                b = 1;
                y = a;
            });
            one.start(); other.start();
            one.join(); other.join();
            String result = "第" + i + "次(" + x + "," + y + ")";
            if (x == 0 && y == 0) {
                System.err.println(result);
                break;
            } else {
                //System.out.println(result);
            }
        }
    }

    private static void shortWait(long interval) {
        long start = System.nanoTime();
        long end;
        do {
            end = System.nanoTime();
        } while ((start + interval) > end);
    }
}

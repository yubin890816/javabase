package com.yubin.jvm.falsesharing;

/**
 * 缓存行案例演示
 * arr[0] 和 arr[1]不在同一个缓存行
 *
 * @author YUBIN
 * @create 2020-10-18
 */
public class T02_CacheLinePadding {

    private static class Padding {
        //public volatile long p1, p2, p3, p4, p5, p6, p7;
    }

    private static class T {
        public volatile long p1, p2, p3, p4, p5, p6, p7;
        public volatile long x = 0L;
        public volatile long p11, p12, p13, p14, p15, p16, p17;
    }

    public static T[] arr = new T[2];

    static {
        arr[0] = new T();
        arr[1] = new T();
    }

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(()-> {
            for (long i = 0; i < 10000_0000L; i++) {
                arr[0].x = i;
            }
        }
        );

        Thread t2 = new Thread(()-> {
            for (long i = 0; i < 10000_0000L; i++) {
                arr[1].x = i;
            }
        }
        );

        final long startTime = System.nanoTime();
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println((System.nanoTime() - startTime) / 100_0000);
    }
}

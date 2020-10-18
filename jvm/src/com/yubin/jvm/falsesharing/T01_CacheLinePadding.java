package com.yubin.jvm.falsesharing;

/**
 * 缓存行案例演示
 *  arr[0] 和 arr[1]位于同一个缓存行
 *
 * @author YUBIN
 * @create 2020-10-18
 */
public class T01_CacheLinePadding {
    private static class T {
        public volatile long x = 0L;
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

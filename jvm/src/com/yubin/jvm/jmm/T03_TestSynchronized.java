package com.yubin.jvm.jmm;

/**
 * 验证Synchronized关键字底层实现细节
 *
 * @author YUBIN
 * @create 2020-10-21
 */
public class T03_TestSynchronized {
    synchronized void m() {

    }

    void n() {
        synchronized (this) {

        }
    }
}

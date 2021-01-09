package com.yubin.io.test_reactor;

/**
 * 主线程
 * 这里不做关于 IO 和 业务的事情
 *
 * @author YUBIN
 * @create 2021-01-07
 */
public class MainTread {

    public static void main(String[] args) {
        // 1、创建IO Thread(一个或者多个)
        //SelectorThreadGroup stg = new SelectorThreadGroup(1);
        //SelectorThreadGroup stg = new SelectorThreadGroup(3);

        SelectorThreadGroup boss = new SelectorThreadGroup(1);
        SelectorThreadGroup worker = new SelectorThreadGroup(3);
        boss.setWorker(worker);
        // 2、把监听的server注册到某一个selector上
        boss.bind(9999);
    }
}

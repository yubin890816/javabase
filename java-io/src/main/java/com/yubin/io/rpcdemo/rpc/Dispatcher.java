package com.yubin.io.rpcdemo.rpc;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 服务端的服务注册类
 *
 * @author YUBIN
 * @create 2021-01-17
 */
public class Dispatcher {

    private Dispatcher(){}

    private static Dispatcher dis = new Dispatcher();

    public static Dispatcher getInstance() {
        return dis;
    }

    public static ConcurrentHashMap<String, Object> invokeMap = new ConcurrentHashMap<>();

    public void register(String k, Object obj) {
        invokeMap.put(k, obj);
    }

    public Object get(String k) {
        return invokeMap.get(k);
    }
}

package com.yubin.io.rpcdemo.rpc;

import com.yubin.io.rpcdemo.util.PackMsg;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 客户端收到服务端的响应映射回调处理类
 *
 * @author YUBIN
 * @create 2021-01-17
 */
public class ResponseMappingCallback {
    //private static final ConcurrentHashMap<Long, Runnable> mapping = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<Long, CompletableFuture> mapping = new ConcurrentHashMap<>();

    public static void addCallback(Long requestId, CompletableFuture callback) {
        mapping.put(requestId, callback);
    }

    public static void runCallback(PackMsg pkg) {
        CompletableFuture future = mapping.get(pkg.getHeader().getRequestID());
        //runnable.run();
        future.complete(pkg.getContent().getRes());
        removeCallback(pkg.getHeader().getRequestID());
    }

    public static void removeCallback(Long requestId) {
        mapping.remove(requestId);
    }
}

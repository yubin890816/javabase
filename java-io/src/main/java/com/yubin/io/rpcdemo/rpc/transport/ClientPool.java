package com.yubin.io.rpcdemo.rpc.transport;

import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * 客户端连接池
 *
 * @author YUBIN
 * @create 2021-01-17
 */
public class ClientPool {
    NioSocketChannel[] clients;

    Object[] lock;

    ClientPool(int size){
        clients = new NioSocketChannel[size];//init  初始的时候只是初始了数组对象,连接都是空的,未来谁第一次用谁创建
        lock = new Object[size]; //锁是可以初始化的
        for(int i = 0;i< size;i++){
            lock[i] = new Object();
        }

    }
}

package com.yubin.io.rpcdemo.rpc.transport;

import com.yubin.io.rpcdemo.rpc.ResponseMappingCallback;
import com.yubin.io.rpcdemo.rpc.protocol.MyContent;
import com.yubin.io.rpcdemo.rpc.protocol.MyHeader;
import com.yubin.io.rpcdemo.util.SerDerUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 客户端工厂
 *  使用单例设计模式-饿汉式
 * @author YUBIN
 * @create 2021-01-17
 */
public class ClientFactory {
    // 一个consumer 可以连接很多的provider，每一个provider都有自己的pool  K,V
    private ConcurrentHashMap<InetSocketAddress, ClientPool> outboxs = new ConcurrentHashMap<>();

    private ClientFactory(){}

    private static final ClientFactory factory = new ClientFactory();
    /*static {
        factory = new ClientFactory();
    }*/
    public static ClientFactory getFactory(){
        return factory;
    }

    public static CompletableFuture<Object> transport(MyContent content) {
        byte[] msgBody = SerDerUtil.ser(content);

        // 2、requestId + message (requestId唯一,同时本地应该有缓存, 方便一个请求和响应对应上(即响应回来的数据能找到是哪个请求发送出去的))
        //协议: 【header<>】【msgBody】
        MyHeader header = MyHeader.createHeader(msgBody);
        byte[] msgHeader = SerDerUtil.ser(header);

        //System.out.println("main::::" + msgHeader.length);
        // 注册一个回调
        //CountDownLatch countDownLatch = new CountDownLatch(1);
        long requestId = header.getRequestID();
        CompletableFuture<Object> res = new CompletableFuture<>();
        ResponseMappingCallback.addCallback(requestId, res);

        //ClientFactory factory = ClientFactory.getFactory();
        NioSocketChannel clientChannel = factory.getClient(new InetSocketAddress("localhost", 9090));

        // 4、发送请求(走的是IO out -> 底层走netty)
        ByteBuf byteBuf = PooledByteBufAllocator.DEFAULT.directBuffer(msgHeader.length + msgBody.length);
        byteBuf.writeBytes(msgHeader);
        byteBuf.writeBytes(msgBody);
        clientChannel.writeAndFlush(byteBuf);
        return res;
    }

    // 连接池中连接的数量
    int poolSize = 5;

    NioEventLoopGroup clientWorker;

    Random rand = new Random();

    public NioSocketChannel getClient(InetSocketAddress address){
        // 先从本地缓存中获取连接池
        ClientPool clientPool = outboxs.get(address);
        // 本地缓存中不存在该Provider对应的连接池, 则创建一个
        if(clientPool ==  null){
            synchronized (outboxs) {
                if (clientPool == null) {
                    outboxs.putIfAbsent(address, new ClientPool(poolSize));
                }
                clientPool = outboxs.get(address);
            }
        }

        // 如果连接池中对应的那个连接存在且是active的
        int i = rand.nextInt(poolSize);
        if (clientPool.clients[i] != null && clientPool.clients[i].isActive()) {
            return clientPool.clients[i];
        }

        // 该锁防止其它线程的其它方法往这个池子里创建连接
        synchronized (clientPool.lock[i]){
            if (clientPool.clients[i] != null && clientPool.clients[i].isActive()) {
                return clientPool.clients[i];
            }
            return clientPool.clients[i] = create(address);
        }

    }

    /**
     * 创建并初始化连接池中的某个连接
     * @param address
     * @return
     */
    private NioSocketChannel create(InetSocketAddress address){
        // 基于 netty 的客户端创建方式
        clientWorker = new NioEventLoopGroup(1);
        Bootstrap bs = new Bootstrap();
        ChannelFuture connect = bs.group(clientWorker)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ChannelPipeline p = ch.pipeline();
                        p.addLast(new ServerDecode()); // 复用服务端的解码器
                        p.addLast(new ClientResponses());  // 该Handler需要解决解决给谁的？？  requestID..
                    }
                }).connect(address);
        try {
            NioSocketChannel client = (NioSocketChannel)connect.sync().channel();
            return client;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;


    }
}

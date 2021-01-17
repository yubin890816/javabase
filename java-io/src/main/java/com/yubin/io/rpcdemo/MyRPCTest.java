package com.yubin.io.rpcdemo;

import com.yubin.io.rpcdemo.proxy.MyProxy;
import com.yubin.io.rpcdemo.rpc.Dispatcher;
import com.yubin.io.rpcdemo.rpc.transport.ServerDecode;
import com.yubin.io.rpcdemo.rpc.transport.ServerRequestHandler;
import com.yubin.io.rpcdemo.service.Car;
import com.yubin.io.rpcdemo.service.MyCar;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 基于netty手写一个RPC框架
 * RPC: 通俗点点解释就像调用本地方法一样去调用远程的服务, 对于java来说就是所谓的面向所谓的接口开发
 *
 * 书写一个RPC框架需要考虑的事情
 *  1: 客户端服务端肯定得来回通信(请求、响应)，连接数量如何设置，如何进行拆包？
 *  2: 客户端调用服务端像调用本地方法一样肯定得需要用到动态代理， 通信的数据需要序列化反序列化，协议的封装等问题
 *  3: 连接池管理
 * @author YUBIN
 * @create 2021-01-12
 */
public class MyRPCTest {

    @Test
    public void test() {
        InetSocketAddress address1 = new InetSocketAddress("localhost", 9090);
        InetSocketAddress address2 = new InetSocketAddress("localhost", 9090);
        System.out.println(address1.equals(address2));
    }

    @Test
    public void startServer(){
        // 注册对外提供的服务
        Car car = new MyCar();
        Dispatcher dispatcher = Dispatcher.getInstance();
        dispatcher.register(Car.class.getName(), car);

        NioEventLoopGroup boss = new NioEventLoopGroup(20);
        NioEventLoopGroup worker =  boss;

        ServerBootstrap sbs = new ServerBootstrap();
        ChannelFuture bind = sbs.group(boss, worker)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        System.out.println("server accept client port: "+ ch.remoteAddress().getPort());
                        ChannelPipeline p = ch.pipeline();
                        p.addLast(new ServerDecode()); // 解码器
                        p.addLast(new ServerRequestHandler(dispatcher)); // 解析每一条业务
                    }
                }).bind(new InetSocketAddress("localhost", 9090));
        try {
            bind.sync().channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    /**
     * 模拟Consumer
     */
    @Test
    public void clientTest() {

        /*new Thread(()->{
            startServer();
        }).start();*/

        System.out.println("服务端启动成功...");
        //Car car = proxyGet(Car.class);
        //car.ooxx("hello rpc");

        // 获取到的其实是一个代理对象,底层封装了对远程服务的调用
        int size = 20;
        AtomicInteger atomicInteger = new AtomicInteger();
        Thread[] threads = new Thread[size];
        for (int i = 0; i < size; i++) {
            threads[i] = new Thread(() -> {

                /**
                 * FC: 函数调用(寻址)
                 * SC: 系统调用(中断)
                 * RPC: 一般走的是Socket
                 * IPC: 管道、信号、Socket（同主机的进程间调用）
                 */
                // 思考一下,服务可能是远程的,也可能是本机的
                Car car = MyProxy.proxyGet(Car.class);
                String arg = "hello rpc" + atomicInteger.incrementAndGet();
                String res = car.ooxx(arg);
                System.out.println(String.format("请求参数:%s, 响应:%s", arg, res));
            });
        }
        for (int i = 0; i < size; i++) {
            threads[i].start();
        }
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
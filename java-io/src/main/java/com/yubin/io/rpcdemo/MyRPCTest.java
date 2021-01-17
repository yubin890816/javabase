package com.yubin.io.rpcdemo;

import com.yubin.io.rpcdemo.proxy.MyProxy;
import com.yubin.io.rpcdemo.rpc.Dispatcher;
import com.yubin.io.rpcdemo.rpc.protocol.MyContent;
import com.yubin.io.rpcdemo.service.Car;
import com.yubin.io.rpcdemo.service.MyCar;
import com.yubin.io.rpcdemo.util.SerDerUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;

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
                        // 1、自定义的rpc协议(自己定义协议的时候,关注过哪些问题 粘包拆包问题, header+body)
                        //p.addLast(new ServerDecode()); // 解码器
                        //p.addLast(new ServerRequestHandler(dispatcher)); // 解析每一条业务
                        // 2、传输协议使用的是http(netty提供了一套编解码)
                        p.addLast(new HttpServerCodec())
                                .addLast(new HttpObjectAggregator(1024 * 512))
                                .addLast(new ChannelInboundHandlerAdapter(){
                                    @Override
                                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                        // http协议, 这个msg是什么呢? 完整的http request
                                        FullHttpRequest request = (FullHttpRequest) msg;
                                        System.out.println(request.toString()); // 因为现在consumer使用的是一个现成的URL
                                        ByteBuf content = request.content();
                                        byte[] data = new byte[content.readableBytes()];
                                        content.readBytes(data);
                                        ObjectInputStream oin = new ObjectInputStream(new ByteArrayInputStream(data));
                                        MyContent myContent = (MyContent) oin.readObject();

                                        String serviceName = myContent.getName();
                                        String methodName = myContent.getMethodName();
                                        Object obj = dispatcher.get(serviceName);
                                        Class<?> clazz = obj.getClass();
                                        Object result = null;
                                        try {
                                            Method method = clazz.getMethod(methodName, myContent.getParameterTypes());
                                            result = method.invoke(obj, myContent.getArgs());
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        // 封装返回的内容
                                        MyContent resContent = new MyContent();
                                        resContent.setRes(result);
                                        byte[] msgBody = SerDerUtil.ser(resContent);

                                        DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_0,
                                                HttpResponseStatus.OK, Unpooled.copiedBuffer(msgBody));
                                        // http协议 header+body
                                        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, msgBody.length);
                                        ctx.writeAndFlush(response);
                                    }
                                });
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

        //System.out.println("服务端启动成功...");
        Car car = MyProxy.proxyGet(Car.class);
        String result = car.ooxx("hello rpc");
        System.out.println(result);

        // 获取到的其实是一个代理对象,底层封装了对远程服务的调用
        /*int size = 20;
        AtomicInteger atomicInteger = new AtomicInteger();
        Thread[] threads = new Thread[size];
        for (int i = 0; i < size; i++) {
            threads[i] = new Thread(() -> {

                *//**
                 * FC: 函数调用(寻址)
                 * SC: 系统调用(中断)
                 * RPC: 一般走的是Socket
                 * IPC: 管道、信号、Socket（同主机的进程间调用）
                 *//*
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
        }*/
        /*try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }
}
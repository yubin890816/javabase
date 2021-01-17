package com.yubin.io.rpcdemo.rpc.transport;

import com.yubin.io.rpcdemo.rpc.ResponseMappingCallback;
import com.yubin.io.rpcdemo.rpc.protocol.MyContent;
import com.yubin.io.rpcdemo.rpc.protocol.MyHeader;
import com.yubin.io.rpcdemo.util.SerDerUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.URL;
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
        // content就是货物, 现在可以用自定义的rpc传输协议(有状态), 也可以用http协议作为载体传输
        // 我们先手工用http协议作为载体, 那这样是不是代表我们未来可以让provider是一个tomcat jetty等基于http协议的一个容器
        // 有无状态来自于你使用的什么协议,那么http协议肯定是无状态, 每请求对应一个连接
        // dubbo是一个rpc框架 netty是一个io框架
        // dubbo中传输协议上,可以是自定义的rpc传输协议, 也可以是http协议
        CompletableFuture<Object> res = new CompletableFuture<>();
        //String protocol = "rpc";
        String protocol = "http";
        if (protocol.equals("http")) {
            // 使用http协议载体
            // 1、使用URL 现成的工作(包含了http的编解码, 发送, socket, 连接)
            //urlTransfer(content, res);

            // 2、自己操心: on netty(io 框架) + 已经提供的http相关的编解码
            nettyTransfer(content, res);

        } else if (protocol.equals("rpc")) {
            byte[] msgBody = SerDerUtil.ser(content);

            // 2、requestId + message (requestId唯一,同时本地应该有缓存, 方便一个请求和响应对应上(即响应回来的数据能找到是哪个请求发送出去的))
            //协议: 【header<>】【msgBody】
            MyHeader header = MyHeader.createHeader(msgBody);
            byte[] msgHeader = SerDerUtil.ser(header);

            //System.out.println("main::::" + msgHeader.length);
            // 注册一个回调
            //CountDownLatch countDownLatch = new CountDownLatch(1);
            long requestId = header.getRequestID();

            ResponseMappingCallback.addCallback(requestId, res);

            //ClientFactory factory = ClientFactory.getFactory();
            NioSocketChannel clientChannel = factory.getClient(new InetSocketAddress("localhost", 9090));

            // 4、发送请求(走的是IO out -> 底层走netty)
            ByteBuf byteBuf = PooledByteBufAllocator.DEFAULT.directBuffer(msgHeader.length + msgBody.length);
            byteBuf.writeBytes(msgHeader);
            byteBuf.writeBytes(msgBody);
            clientChannel.writeAndFlush(byteBuf);
        }
        return res;
    }

    private static void nettyTransfer(MyContent content, CompletableFuture<Object> res) {
        // 在这个执行之前 我们的server端 provider端已经开发完了, 已经是on netty的http server了
        // 现在做的是consumer端的代码修改, 改成on netty的http client
        // 刚从一切都顺利, 关注未来的问题...

        // 每个请求对应一个连接
        // 1、通过netty建立io 建立连接
        NioEventLoopGroup group = new NioEventLoopGroup(1); // 定义到外面
        Bootstrap bs = new Bootstrap();
        Bootstrap client = bs.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ChannelPipeline p = ch.pipeline();
                        p.addLast(new HttpClientCodec())
                                .addLast(new HttpObjectAggregator(1024 * 512))
                                .addLast(new ChannelInboundHandlerAdapter() {
                                    @Override
                                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                        // 3、接收 预埋的回调, 根据netty对socket io 事件的响应
                                        // 客户端的msg是撒: 完整的http response
                                        FullHttpResponse response = (FullHttpResponse) msg;
                                        System.out.println(response);
                                        ByteBuf resContent = response.content();
                                        byte[] data = new byte[resContent.readableBytes()];
                                        resContent.readBytes(data);
                                        ObjectInputStream oin = new ObjectInputStream(new ByteArrayInputStream(data));
                                        MyContent resObj = (MyContent) oin.readObject();
                                        res.complete(resObj.getRes());
                                    }

                                });

                    }
                });// 未来连接后, 收到数据的处理handler
        // 2、发送
        try {
            ChannelFuture syncFuture = client.connect("localhost", 9090).sync();
            Channel clientChannel = syncFuture.channel();
            byte[] data = SerDerUtil.ser(content);
            DefaultFullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_0,
                    HttpMethod.POST, "/", Unpooled.copiedBuffer(data));
            request.headers().set(HttpHeaderNames.CONTENT_LENGTH, data.length);
            clientChannel.writeAndFlush(request).sync(); // 作为client向server端发送http request
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    private static void urlTransfer(MyContent content, CompletableFuture<Object> res) {
        Object obj = null;
        try {
            URL url = new URL("http://localhost:9090/");
            HttpURLConnection hc = (HttpURLConnection) url.openConnection();
            // POST请求
            hc.setRequestMethod("POST");
            hc.setDoOutput(true);
            hc.setDoInput(true);

            OutputStream out = hc.getOutputStream();
            ObjectOutputStream oout = new ObjectOutputStream(out);
            oout.writeObject(content); // 这里真的发送了么? 没有

            if (hc.getResponseCode() == 200) { // 这一步才真正完成发送
                InputStream in = hc.getInputStream();
                ObjectInputStream oin = new ObjectInputStream(in);
                MyContent myContent = (MyContent) oin.readObject();
                obj = myContent.getRes();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        res.complete(obj);
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

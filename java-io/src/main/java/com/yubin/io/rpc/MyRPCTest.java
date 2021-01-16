package com.yubin.io.rpc;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.junit.Test;

import java.io.*;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
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
                        p.addLast(new ServerRequestHandler()); // 解析每一条业务
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
    public void get() {

        new Thread(()->{
            startServer();
        }).start();

        System.out.println("服务端启动成功...");
        //Car car = proxyGet(Car.class);
        //car.ooxx("hello rpc");

        // 获取到的其实是一个代理对象,底层封装了对远程服务的调用
        int size = 20;
        AtomicInteger atomicInteger = new AtomicInteger();
        Thread[] threads = new Thread[size];
        for (int i = 0; i < size; i++) {
            threads[i] = new Thread(() -> {
                Car car = proxyGet(Car.class);
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

    public static <T> T proxyGet(Class<T>  interfaceClass){
        // 实现各个版本的动态代理...(该案例使用的是JDK自带的jdk动态代理)
        ClassLoader loader = interfaceClass.getClassLoader();
        Class<?>[] interfaces = {interfaceClass};
        return (T) Proxy.newProxyInstance(loader, interfaces, new InvocationHandler() {

            /**
             * 通过该代理如何设计我们的Consumer对于Provider的调用过程?
             */
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                // 1、需要封装的数据是调用的服务、方法、参数  将其 封装成message  [content]
                // 类的全限定名
                String name = interfaceClass.getName();
                // 方法名称
                String methodName = method.getName();
                Class<?>[] parameterTypes = method.getParameterTypes();
                MyContent content = new MyContent();
                content.setArgs(args);
                content.setName(name);
                content.setMethodName(methodName);
                content.setParameterTypes(parameterTypes);
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                ObjectOutputStream oout = new ObjectOutputStream(out);
                oout.writeObject(content);
                byte[] msgBody = out.toByteArray();

                // 2、requestId + message (requestId唯一,同时本地应该有缓存, 方便一个请求和响应对应上(即响应回来的数据能找到是哪个请求发送出去的))
                //协议: 【header<>】【msgBody】
                MyHeader header = createHeader(msgBody);
                out.reset();
                oout = new ObjectOutputStream(out);
                oout.writeObject(header);
                // 解决数据decode问题
                byte[] msgHeader = out.toByteArray();

                System.out.println(String.format("client send msgHeader length:%s,message length:%s, requestId:%s", msgHeader.length, msgHeader.length + msgBody.length, header.getRequestID()));

                // 3、连接池(取得连接)
                ClientFactory factory = ClientFactory.getFactory();
                NioSocketChannel clientChannel = factory.getClient(new InetSocketAddress("localhost", 9090));

                // 注册一个回调
                //CountDownLatch countDownLatch = new CountDownLatch(1);
                long requestId = header.getRequestID();
                CompletableFuture<String> res = new CompletableFuture<>();
                ResponseMappingCallback.addCallback(requestId, res);

                // 4、发送请求(走的是IO out -> 底层走netty)
                ByteBuf byteBuf = PooledByteBufAllocator.DEFAULT.directBuffer(msgHeader.length + msgBody.length);
                byteBuf.writeBytes(msgHeader);
                byteBuf.writeBytes(msgBody);
                ChannelFuture channelFuture = clientChannel.writeAndFlush(byteBuf);
                channelFuture.sync();  //IO是双向的，你看似有个sync，她仅代表out
                // 5、？如果从IO，未来有响应了, 怎么将代码执行到这里(执行完发送之后,线程处于阻塞状态,有响应了唤醒)(睡眠/回调)
                //countDownLatch.await();
                return res.get();//阻塞的
            }
        });


    }

    public static MyHeader createHeader(byte[] msg){
        MyHeader header = new MyHeader();
        int size = msg.length;
        int f = 0x14141414;
        long requestID =  Math.abs(UUID.randomUUID().getLeastSignificantBits());
        //0x14  0001 0100
        header.setFlag(f);
        header.setDataLen(size);
        header.setRequestID(requestID);
        return header;
    }
}

class ResponseMappingCallback {
    //private static final ConcurrentHashMap<Long, Runnable> mapping = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<Long, CompletableFuture> mapping = new ConcurrentHashMap<>();

    public static void addCallback(Long requestId, CompletableFuture callback) {
        mapping.put(requestId, callback);
    }

    public static void runCallback(PackMsg pkg) {
        CompletableFuture future = mapping.get(pkg.getHeader().requestID);
        //runnable.run();
        future.complete(pkg.getContent().getRes());
        removeCallback(pkg.getHeader().requestID);
    }

    public static void removeCallback(Long requestId) {
        mapping.remove(requestId);
    }
}

/**
 * 解码器(本质上也是一个Handler)
 * 父类里一定有channelRead方法(老的buf在方法执行前拼到新的buf中; 新的buf执行剩下的留存到下一个channelRead里面去; 同时将读取到的每一个message放到list中去)
 */
class ServerDecode extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf buf, List<Object> out) throws Exception {
        String logId = UUID.randomUUID().toString();
        System.out.println(String.format("logId:%s server channel accept start 可读字节数:%s", logId, buf.readableBytes()));
        // 这个95的数据是在客户端发送数据的时候,我打印得到的
        //if (buf.readableBytes() >= 95) {
        while (buf.readableBytes() >= 95) { // 修改成while, 因为从buffer中读取到的可能不止一个客户端线程发送的数据
            byte[] bytes = new byte[95];
            buf.getBytes(buf.readerIndex(), bytes); // 从哪里开始读,读多少 且使用getBytes不会移动指针
            //buf.readBytes(bytes);
            ByteArrayInputStream in = new ByteArrayInputStream(bytes);
            ObjectInputStream oin = new ObjectInputStream(in);
            MyHeader header = (MyHeader) oin.readObject();
            System.out.println(String.format("logId:%s server accept @ requestId:%s,header length is 95, body dataLen:%s", logId, header.getRequestID(), header.getDataLen()));
            // decode 在两个方向都使用
            // 通信的协议
            if (buf.readableBytes() >= (header.getDataLen() + 95)) {
                // 代码执行到这里说明buffer里面剩余的内容够一个body
                // 首先移动指针到body开始的位置
                buf.readBytes(95);
                byte[] data = new byte[(int) header.getDataLen()];
                buf.readBytes(data);
                ByteArrayInputStream din = new ByteArrayInputStream(data);
                ObjectInputStream doin = new ObjectInputStream(din);
                //MyContent content = (MyContent) doin.readObject();
                //System.out.println(String.format("logId:%s 从body中取出的name为%s", logId, content.getName()));
                //out.add(new PackMsg(header, content));
                if (header.getFlag() == 0x14141414) { // 接收的
                    MyContent content = (MyContent) doin.readObject();
                    System.out.println(String.format("logId:%s 从body中取出的name为%s", logId, content.getName()));
                    out.add(new PackMsg(header, content));
                } else if (header.getFlag() == 0x14141424) { // 返回的
                    MyContent content = (MyContent) doin.readObject();
                    out.add(new PackMsg(header, content));
                }
            } else {
                System.out.println(String.format("logId:%s else server body 剩余可读字节数:%s ,消息体应该的字节数:%s", logId, buf.readableBytes(), header.getDataLen()));
                break; // 留余的数据会被放到下一个channelRead
            }
        }
    }
}

class ServerRequestHandler extends ChannelInboundHandlerAdapter{

    // provider端
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        PackMsg requestPkg = (PackMsg) msg;
        //System.out.println("server accept arg " + requestPkg.getContent().getArgs()[0]);

        // 如果假设处理完了,要给客户端返回了~ 你需要注意哪些环节？？？
        // bytebuf
        // 因为是RPC,你得有一个requestId
        // 在client那一侧也要解决解码问题
        // 关注rpc通信协议,  来的时候flag是0x14141414

        // 有新的header + content
        String ioThreadName = Thread.currentThread().getName();
        // 方式一、直接在当前方法处理IO和业务以及返回
        // 方式三、自己创建线程池
        // 方式二、使用netty自己的eventLoop来处理业务及返回 (这种方式 IO线程和exec线程还是同一个)
        //ctx.executor().execute(new Runnable() {
        // 方式四、使用group中的其它线程处理
        ctx.executor().parent().next().execute( new Runnable() { // 使用group中的一个线程来处理业务及响应
            @Override
            public void run() {
                String execThreadName = Thread.currentThread().getName();
                MyContent resContent = new MyContent();
                String res = String.format("io thread: %s, exec thread: %s, from args: %s", ioThreadName, execThreadName, requestPkg.getContent().getArgs()[0]);
                System.out.println(String.format("server response: %s", res));
                resContent.setRes(res);
                byte[] msgBody = SerDerUtil.ser(resContent);

                MyHeader resHeader = new MyHeader();
                resHeader.setRequestID(requestPkg.getHeader().requestID);
                resHeader.setFlag(0x14141424);
                resHeader.setDataLen(msgBody.length);
                byte[] msgHeader = SerDerUtil.ser(resHeader);
                ByteBuf buf = PooledByteBufAllocator.DEFAULT.directBuffer(msgHeader.length + msgBody.length);
                buf.writeBytes(msgHeader);
                buf.writeBytes(msgBody);

                ctx.writeAndFlush(buf);
            }
        });

    }

}

/**
 * 连接工厂使用单例设计模式-饿汉式
 */
class ClientFactory{
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

    // 连接池中连接的数量
    int poolSize = 10;

    NioEventLoopGroup clientWorker;

    Random rand = new Random();



    public synchronized NioSocketChannel getClient(InetSocketAddress address){
        // 先从本地缓存中获取连接池
        ClientPool clientPool = outboxs.get(address);
        // 本地缓存中不存在该Provider对应的连接池, 则创建一个
        if(clientPool ==  null){
            System.out.println("=================================================");
            outboxs.putIfAbsent(address, new ClientPool(poolSize));
            clientPool = outboxs.get(address);
        }

        // 如果连接池中对应的那个连接存在且是active的
        int i = rand.nextInt(poolSize);
        if( clientPool.clients[i] != null && clientPool.clients[i].isActive()){
            return clientPool.clients[i];
        }

        // 该锁防止其它线程的其它方法往这个池子里创建连接
        synchronized (clientPool.lock[i]){
            if( clientPool.clients[i] != null && clientPool.clients[i].isActive()){
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

class ClientPool{
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

class ClientResponses extends ChannelInboundHandlerAdapter {

    //consumer.....
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        PackMsg responsePkg = (PackMsg) msg;
        ResponseMappingCallback.runCallback(responsePkg);
    }
}

/**
 * 通信上的协议
 */
class MyHeader implements Serializable{
    /**
     * 1: ooxx值(标识是什么样的协议(二进制值))
     * 2: UUID: requestID
     * 3: DATA_LEN
     */
    int flag;  // 32bit可以设置很多信息。。。
    long requestID; // 请求的唯一标识
    long dataLen; // 消息体的字节数


    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public long getRequestID() {
        return requestID;
    }

    public void setRequestID(long requestID) {
        this.requestID = requestID;
    }

    public long getDataLen() {
        return dataLen;
    }

    public void setDataLen(long dataLen) {
        this.dataLen = dataLen;
    }
}

class MyContent implements Serializable {
    private String name; // 接口全限定名
    private String methodName; // 调用的方法名称
    private Class<?>[] parameterTypes; // 方法参数类型
    private Object[] args; // 参数列表
    private String res; // 服务端的响应

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public String getRes() {
        return res;
    }

    public void setRes(String res) {
        this.res = res;
    }
}

interface Car {
    public String ooxx(String msg);
}

interface Fly{
    void xxoo(String msg);
}
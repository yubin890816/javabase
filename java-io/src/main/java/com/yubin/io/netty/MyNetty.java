package com.yubin.io.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;
import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * netty初入门
 *
 * @author YUBIN
 * @create 2021-01-09
 */
public class MyNetty {

    /**
     * 认识一下Netty中的ByteBuf类
     *  该类是对NIO中的ByteBuffer的封装, 它省去了操作ByteBuffer中pos、lim、cap的复杂操作
     *  并提供有pool的概念
     */
    @Test
    public void testByteBuf() {
        //ByteBuf buf = ByteBufAllocator.DEFAULT.buffer(8, 20);
        // pool
        //ByteBuf buf = UnpooledByteBufAllocator.DEFAULT.heapBuffer(8, 20); // 堆内分配
        ByteBuf buf = PooledByteBufAllocator.DEFAULT.heapBuffer(8, 20); // 堆内分配
        print(buf);
        buf.writeBytes(new byte[]{1, 2, 3, 4});
        print(buf);
        buf.writeBytes(new byte[]{1, 2, 3, 4});
        print(buf);
        buf.writeBytes(new byte[]{1, 2, 3, 4});
        print(buf);
        buf.writeBytes(new byte[]{1, 2, 3, 4});
        print(buf);
        buf.writeBytes(new byte[]{1, 2, 3, 4});
        print(buf);
        // 写的时候超出maxCapacity的话,会抛出java.lang.IndexOutOfBoundsException异常
        buf.writeBytes(new byte[]{1, 2, 3, 4});
        print(buf);
    }

    private void print(ByteBuf buf) {
        System.out.println("buf是否可读: " + buf.isReadable());
        System.out.println("此时buf中读的索引位置: " + buf.readerIndex());
        System.out.println("buf中可读的字节数: " + buf.readableBytes());
        System.out.println("buf是否可写: " + buf.isWritable());
        System.out.println("此时buf中写的索引位置: " + buf.writerIndex());
        System.out.println("buf中可写的字节数: " + buf.writableBytes());
        System.out.println("buf的当前容量: " + buf.capacity());
        System.out.println("buf的最大容量: " + buf.maxCapacity());
        System.out.println("buf是否直接(堆外)分配: " + buf.isDirect());
        System.out.println("========================华丽的分割线========================");
    }

    /**
     * NioEventLoopGroup初识
     * @throws IOException
     */
    @Test
    public void testNioEventLoopGroup() throws IOException {
        // group是一个线程池
        //NioEventLoopGroup selector = new NioEventLoopGroup(1);
        NioEventLoopGroup selector = new NioEventLoopGroup(2);
        selector.execute(() -> {
            for (; ; ) {
                try {
                    System.out.println("hello world001");
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        selector.execute(() -> {
            for (; ; ) {
                try {
                    System.out.println("hello world002");
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        System.in.read();
    }

    /**
     * 在xshell 上开启一个nc服务 nc -l 192.168.254.5 9090
     * Netty的客户端模型
     * 客户端连接别人发送数据的两种情况
     *  1、主动发送数据
     *  2、别人什么时候发给我? event selector
     */
    @Test
    public void clientMode() throws InterruptedException {
        NioEventLoopGroup thread = new NioEventLoopGroup(1);

        NioSocketChannel client = new NioSocketChannel();
        /**
         * netty中需要将Channel注册到一个EventLoop上,否则会抛出java.lang.IllegalStateException: channel not registered to an event loop
         * 相当于 epoll_ctl(5,ADD,3)
         */
        thread.register(client);

        // 响应服务端发来的数据
        ChannelPipeline p = client.pipeline();
        p.addLast(new MyInHandler());


        // 响应式编程Reactor 异步的, 所以应该等待连接建立完成再执行后续的代码
        ChannelFuture connect = client.connect(new InetSocketAddress("192.168.254.5", 9090));
        ChannelFuture sync = connect.sync();// 等待连接建立成功

        // 向服务端发送一些数据
        ByteBuf buf = Unpooled.copiedBuffer("hello netty client".getBytes());
        ChannelFuture send = client.writeAndFlush(buf);
        send.sync(); // 数据发送成功再继续往下执行

        // 等待服务器端关闭这个连接
        sync.channel().closeFuture().sync();
        System.out.println("client close...");
    }

    @Test
    public void nettyClient() throws InterruptedException {

        NioEventLoopGroup group = new NioEventLoopGroup(1);
        Bootstrap bs = new Bootstrap();
        ChannelFuture connect = bs.group(group)
                .channel(NioSocketChannel.class)
                //.handler(new ChannelInit())
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel channel) throws Exception {
                        ChannelPipeline p = channel.pipeline();
                        p.addLast(new MyInHandler());
                    }
                })
                .connect(new InetSocketAddress("192.168.254.5", 9090));
        Channel client = connect.sync().channel();

        ByteBuf buf = Unpooled.copiedBuffer("hello netty client".getBytes());
        ChannelFuture send = client.writeAndFlush(buf);
        send.sync();

        client.closeFuture().sync();
        System.out.println("netty client closed...");
    }

    /**
     * Netty的服务端模型
     */
    @Test
    public void serverMode() throws InterruptedException {
        NioEventLoopGroup thread = new NioEventLoopGroup(1);

        NioServerSocketChannel server = new NioServerSocketChannel();
        thread.register(server);

        // 响应客户端的通信
        ChannelPipeline p = server.pipeline();
        p.addLast(new MyAcceptHandler(thread, new ChannelInit()));


        ChannelFuture bind = server.bind(new InetSocketAddress(9090));

        bind.sync().channel().closeFuture().sync();
        System.out.println("server close...");
    }

    @Test
    public void nettyServer() throws InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup();
        ServerBootstrap bs = new ServerBootstrap();
        ChannelFuture bind = bs.group(group, group)
                .channel(NioServerSocketChannel.class)
                //.childHandler(new ChannelInit())
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel channel) throws Exception {
                        ChannelPipeline p = channel.pipeline();
                        p.addLast(new MyInHandler());
                    }
                })
                .bind(new InetSocketAddress(9090));

        bind.sync().channel().closeFuture().sync();
    }
}

class MyAcceptHandler extends ChannelInboundHandlerAdapter {

    private final NioEventLoopGroup selector;

    private final ChannelHandler handler;

    public MyAcceptHandler(NioEventLoopGroup selector, ChannelHandler handler) {
        this.selector = selector;
        this.handler = handler;
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("server register...");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // listen socket accept client
        SocketChannel client = (SocketChannel) msg; // 疑问: 我怎么不需要调用accept呢?

        // 响应式的handler
        ChannelPipeline p = client.pipeline();
        //1、client::pipeline[ChannelInit,]
        p.addLast(handler);

        // 注册
        selector.register(client);

        super.channelRead(ctx, msg);
    }
}

/**
 * 当有多个客户端连接进ServerMode的时候,抛出如下异常:
 * io.netty.channel.ChannelPipelineException: com.yubin.io.netty.MyInHandler is not a @Sharable handler, so can't be added or removed multiple times.
 * 解决方案一: 在MyInHandler类上面添加一个@Sharable注解
 *
 * 由于MyInHandler这个类里面书写的都是一些业务代码,是以后用户书写业务逻辑的类,由用户自己实现,这样每个Handler都要加上@Sharable注解显然不是一个好办法
 * 基于这种原因我们包装一个最外层的Sharable的Handler不就可以了么
 * 解决方案见ChannelInit类
 */
//@ChannelHandler.Sharable
class MyInHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("client register...");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("client active...");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 读取数据
        ByteBuf buf = (ByteBuf) msg;
        //CharSequence str = buf.readCharSequence(buf.readableBytes(), CharsetUtil.UTF_8);
        CharSequence str = buf.getCharSequence(0, buf.readableBytes(), CharsetUtil.UTF_8);
        System.out.println(str);

        // 响应数据给服务端
        ctx.writeAndFlush(buf);
    }
}

/**
 * 为啥要有一个ChannelInit，可以没有，但是MyInHandler就得设计成单例
 */
@ChannelHandler.Sharable
class ChannelInit extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        Channel client = ctx.channel();
        ChannelPipeline p = client.pipeline();
        //2、client::pipeline[ChannelInit,MyInHandler]
        p.addLast(new MyInHandler());
        // 3、client::pipeline[MyInHandler]
        ctx.pipeline().remove(this);
    }
}

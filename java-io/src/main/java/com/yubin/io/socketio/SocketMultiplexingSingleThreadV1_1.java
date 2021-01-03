package com.yubin.io.socketio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * 多路复用+写事件
 *
 * @author YUBIN
 * @create 2021-01-03
 */
public class SocketMultiplexingSingleThreadV1_1 {

    private ServerSocketChannel server = null;

    // java 中将多路复用器抽象成Selector对象 对应Linux多路复用器中的select、poll、epoll 默认使用的是epoll
    private Selector selector = null;

    private int port = 9090;

    /**
     * 初始化服务端
     */
    private void initServer() {
        try {
            server = ServerSocketChannel.open();
            server.configureBlocking(false); // 非阻塞
            server.bind(new InetSocketAddress(port));
            // select、poll、epoll 如果在epoll模型下 open 相当于 epoll_create 假设得到结果 fd3
            selector = Selector.open();
            /**
             * register 注册接收连接事件
             * 如果是 select、poll模型下：在jvm层面开辟一个空间将fd4放进去
             * 如果是 epoll模型下: epoll_ctl(fd3,ADD,fd4,EPOLLIN) 该步是在首次selector.select()时候触发的
             */
            server.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("register listen over...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 启动服务端
     */
    public void start() {

        initServer();
        System.out.println("服务端启动成功...");
        try {
            while (true) {
                /**
                 * 调用多路复用器(select、poll or epoll(epoll_wait))
                 * select()是撒意思:
                 *  对于select、poll模型其实是内核的select(fds)、poll(fds)
                 *  对于epoll型其实是内核的epoll_wait()
                 * select()参数可以带时间: 没有时间表示阻塞, 有时间的话表示超时时间
                 *
                 * 懒加载：
                 *  其实再触碰到selector.select()调用的时候触发了epoll_ctl的调用
                 */
                while (selector.select() > 0) {
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> iter = selectionKeys.iterator();
                    /**
                     * so, 管你啥多路复用器,你呀只能给我状态,我还得一个一个的去处理它们的R/W。同步在这方面的处理确实挺辛苦!
                     * NIO:自己对着每一个fd调用系统调用，浪费资源，
                     * 多路复用器:这里是不是调用了一次select方法，知道具体的那些IO可以R/W了
                     */
                    while (iter.hasNext()) {
                        SelectionKey key = iter.next();
                        iter.remove();
                        if (key.isAcceptable()) {
                            /**
                             * 这里是重点:
                             * 如果要接收一个新的连接, 语义上 accept接收连接且返回新连接的FD,那么新的FD怎么办?
                             *  对于select、poll模型: 因为它们内核没有空间,那么在jvm中保存和前边的fd4那个listen在一起
                             *  对于epoll模型: 我们希望通过epoll_ctl把新的客户端fd注册到内核空间
                             */
                            acceptHandler(key);
                        } else if (key.isConnectable()) {
                            /**
                             * 这里是处理Read 并注册 关心这个key的write事件
                             * 在当前线程，这个方法可能会阻塞  ，如果阻塞了十年，其他的IO早就没电了。。。
                             * 所以，为什么提出了 IO THREADS
                             * redis  是不是用了epoll，redis是不是有个io threads的概念 ，redis是不是单线程的
                             * tomcat 8,9  异步的处理方式  IO和处理上解耦
                             */
                            readHandler(key);
                        } else if (key.isWritable()) {
                            /**
                             *  写事件: send-queue只要是空的，就一定会给你返回可以写的事件，就会回调我们的写方法writeHandler(key)
                             *  你真的要明白：你想什么时候写？不是依赖send-queue是不是有空间（多路复用器能不能写,是参考send-queue有没有空间）
                             *  1、准备好要写什么,这是第一步
                             *  2、第二步才是关心send-queue是否有空间
                             *  3、so,读read一开始就要注册,但是write依赖以上关系,什么时候用什么时候注册
                             *  4、如果一开始就注册write事件,进入死循环,一直调起
                             */
                            writeHandler(key);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 建立连接的处理类
     * @param key
     */
    private void acceptHandler(SelectionKey key) {
        try {
            ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
            // 目的是调用accept接收客户端 fd7
            SocketChannel client = ssc.accept();
            client.configureBlocking(false);
            ByteBuffer buffer = ByteBuffer.allocate(8192);
            /**
             * register
             * 对于select、poll模型: jvm里开辟一个空间将fd7放进去
             * 对于epoll模型: epoll_ctl(fd3,ADD,fd7,EPOLLIN)
             */
            client.register(selector, SelectionKey.OP_READ, buffer);
            System.out.println("-------------------------------------------");
            System.out.println("新客户端：" + client.getRemoteAddress());
            System.out.println("-------------------------------------------");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readHandler(SelectionKey key) {
        System.out.println("read handler.....");
        SocketChannel client = (SocketChannel) key.channel();
        ByteBuffer buffer = (ByteBuffer) key.attachment();
        buffer.clear();
        int read = 0;
        try {
            while (true) {
                read = client.read(buffer);
                if (read > 0) {
                    System.out.println(key.selector().equals(selector));
                    client.register(key.selector(), SelectionKey.OP_WRITE, buffer);
                    //关心  OP_WRITE 其实就是关系send-queue是不是有空间
                } else if (read == 0) {
                    break;
                } else {
                    client.close();
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeHandler(SelectionKey key) {
        System.out.println("write handler...");
        SocketChannel client = (SocketChannel) key.channel();
        ByteBuffer buffer = (ByteBuffer) key.attachment();
        buffer.flip();
        while (buffer.hasRemaining()) {
            try {
                client.write(buffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        buffer.clear();
        key.cancel(); // 取消写事件
        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SocketMultiplexingSingleThreadV1_1 service = new SocketMultiplexingSingleThreadV1_1();
        service.start();
    }
}

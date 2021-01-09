package com.yubin.io.test_reactor;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 多路复用器的线程
 * 每个线程对应一个Selector, 多线程情况下, 该主机, 该程序的并发客户端被分配到多个Selector
 * 注意: 每个客户端, 只绑定到一个Selector（言外之意是不会有交互问题）
 *
 * @author YUBIN
 * @create 2021-01-07
 */
public class SelectorThread implements Runnable {

    // 多路复用器
    public Selector selector;

    public LinkedBlockingQueue<Channel> lbq = new LinkedBlockingQueue<>();

    private SelectorThreadGroup stg;

    public SelectorThread(SelectorThreadGroup stg) {
        this.stg = stg;
        try {
            selector = Selector.open();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        // Loop = while(true)
        while (true) {
            try {
                // 1、select() 阻塞(被唤醒则说明有些FD有事件了) 当调用wakeup()的时候,也会跳出阻塞,此时num>0才会不成立
                System.out.println(Thread.currentThread().getName() + ": before select...." + selector.keys().size());
                int nums = selector.select();
                /*try {
                    Thread.sleep(1000); // 这并非解决方案
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/
                System.out.println(Thread.currentThread().getName() + ": after select...." + selector.keys().size());
                // 2、处理selectKeys
                if (nums > 0) {
                    Set<SelectionKey> keys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = keys.iterator();
                    // 逐一线性处理
                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();
                        iterator.remove();
                        if (key.isAcceptable()) { // 建立连接的事件
                            // 复杂, 接收客户端的过程(接收之后,要注册, 多线程情况下,新的客户端注册到哪个Selector呢？)
                            acceptHandler(key);
                        } else if (key.isReadable()) { // 可读
                            readHandler(key);
                        } else if (key.isWritable()) { // 可写

                        }
                    }
                }
                // 3、处理一些task
                if (!lbq.isEmpty()) {
                    Channel channel = lbq.take();
                    if (channel instanceof ServerSocketChannel) {
                        ServerSocketChannel server = (ServerSocketChannel) channel;
                        server.register(selector, SelectionKey.OP_ACCEPT);
                    } else if (channel instanceof SocketChannel) {
                        SocketChannel client = (SocketChannel) channel;
                        ByteBuffer buffer = ByteBuffer.allocateDirect(4096);
                        client.register(selector, SelectionKey.OP_READ, buffer);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void readHandler(SelectionKey key) {
        ByteBuffer buffer = (ByteBuffer) key.attachment();
        SocketChannel client = (SocketChannel) key.channel();
        buffer.clear();
        while (true) {
            try {
                int num = client.read(buffer); // 将数据读取到buffer里去,返回值是读到的字节数
                if (num > 0) {
                    buffer.flip(); // 将读到的内容翻转,然后直接写出
                    while (buffer.hasRemaining()) {
                        client.write(buffer);
                    }
                    buffer.clear();
                } else if (num == 0) {
                    break;
                } else {
                    // <0 :客户端断开了
                    System.out.println("client:" + client.getRemoteAddress() + "closed...");
                    key.cancel(); // 从多路复用器中取消掉
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void acceptHandler(SelectionKey key) {
        System.out.println(Thread.currentThread().getName()+"   acceptHandler......");

        ServerSocketChannel server = (ServerSocketChannel) key.channel();

        SocketChannel client = null;
        try {
            client = server.accept();
            client.configureBlocking(false);
            // choose a selector and register!
            //stg.nextSelector(client);
            //stg.nextSelectorV2(client);
            stg.nextSelectorV3(client);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

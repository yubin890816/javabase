package com.yubin.io.test_reactor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.Channel;
import java.nio.channels.ServerSocketChannel;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 管理selector组
 *
 * @author YUBIN
 * @create 2021-01-07
 */
public class SelectorThreadGroup {

    private SelectorThread[] sts;

    private ServerSocketChannel server;

    private AtomicInteger xId = new AtomicInteger();

    private SelectorThreadGroup stg = this;

    public void setWorker(SelectorThreadGroup stg) {
        this.stg = stg;
    }

    /**
     * @param num 线程数
     */
    public SelectorThreadGroup(int num) {
        sts = new SelectorThread[num];
        for (int i = 0; i < num; i++) {
            sts[i] = new SelectorThread(this);
            new Thread(sts[i]).start();
        }
    }

    public void bind(int port) {
        try {
            server = ServerSocketChannel.open();
            server.configureBlocking(false);
            server.bind(new InetSocketAddress(port));

            // 将listen 注册到哪个selector上呢?
            //nextSelector(server);
            //nextSelectorV2(server);
            nextSelectorV3(server);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 选择某一Selector
     * ServerSocketChannel 和 SocketChannel都是Channel的子类
     * @param channel
     */
    public void nextSelector(Channel channel) {
        SelectorThread st = next();
        // 不正确的代码
        // 重点: channel可能是server 也有可能是 client
        /*ServerSocketChannel s = (ServerSocketChannel) channel;
        // 呼应上 SelectorThread中有一段代码 int nums = selector.select(); 是阻塞
        try {
            // selector.wakeup() 写在s.register上面的话,可能selector.select();被唤醒之后,register还没有执行另外一个线程的selector.select();的代码又执行了(主要是server还没有注册就阻塞了)
            // 解决方案是selector对应的线程睡一会(显然这个方案在生产上是不行的,只是概率的问题)
            st.selector.wakeup(); // 功能是让 selector.select(); 不阻塞
            //System.out.println("wakeup 被执行了...");
            s.register(st.selector, SelectionKey.OP_ACCEPT); // 会被阻塞的!!!
            //st.selector.wakeup(); // 先注册再wakeup()就更加的不行了(完全没有意义了)
            //System.out.println("wakeup 被执行了...");
        } catch (ClosedChannelException e) {
            e.printStackTrace();
        }*/
        // 1、通过队列传递数据 消息
        st.lbq.add(channel);
        // 2、通过打断阻塞,让对应的线程去自己在打断后完成注册selector
        st.selector.wakeup();
    }

    /**
     * 选择某一Selector
     * ServerSocketChannel 和 SocketChannel都是Channel的子类
     * @param channel
     */
    public void nextSelectorV2(Channel channel) {
        try {
            if (channel instanceof ServerSocketChannel) {
                SelectorThread st = sts[0];
                // 1、通过队列传递数据 消息
                st.lbq.put(channel);
                // 2、通过打断阻塞,让对应的线程去自己在打断后完成注册selector
                st.selector.wakeup();
            } else {
                SelectorThread st = nextV2();
                // 1、通过队列传递数据 消息
                st.lbq.put(channel);
                // 2、通过打断阻塞,让对应的线程去自己在打断后完成注册selector
                st.selector.wakeup();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 选择某一Selector
     * ServerSocketChannel 和 SocketChannel都是Channel的子类
     * @param channel
     */
    public void nextSelectorV3(Channel channel) {
        try {
            if (channel instanceof ServerSocketChannel) {
                SelectorThread st = next();
                // 1、通过队列传递数据 消息
                st.lbq.put(channel);
                // 2、通过打断阻塞,让对应的线程去自己在打断后完成注册selector
                st.selector.wakeup();
            } else {
                SelectorThread st = nextV3();
                // 1、通过队列传递数据 消息
                st.lbq.put(channel);
                // 2、通过打断阻塞,让对应的线程去自己在打断后完成注册selector
                st.selector.wakeup();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private SelectorThread next() {
        int index = xId.incrementAndGet() % sts.length;
        return sts[index];
    }

    private SelectorThread nextV2() {
        // 选择的时候排除第一个
        int index = xId.incrementAndGet() % (sts.length - 1);
        return sts[index+1];
    }

    private SelectorThread nextV3() {
        // 使用worker的线程分配
        int index = stg.xId.incrementAndGet() % stg.sts.length;
        return stg.sts[index];
    }
}

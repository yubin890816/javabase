package com.yubin.io.socketio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;

/**
 * 服务端NIO
 *
 * @author YUBIN
 * @create 2021-01-02
 */
public class SocketNIO {

    public static void main(String[] args) throws IOException, InterruptedException {
        LinkedList<SocketChannel> clients = new LinkedList<>();

        // 服务端开启监听：接受客户端
        ServerSocketChannel ss = ServerSocketChannel.open();
        ss.bind(new InetSocketAddress(9090));
        // 重点  OS  NONBLOCKING!!!
        // 只让接受客户端  不阻塞
        ss.configureBlocking(false);

        while (true) {
            // 1、//接受客户端的连接
            Thread.sleep(1000);
            //不会阻塞  当没有客户端建立连接时操作系统层面返回:-1,java层面返回:NULL
            SocketChannel client = ss.accept();
            if (client == null) { // 没有客户端建立连接
                System.out.println("null...");
            } else { // 说明有客户端建立连接了
                // 重点  socket（服务端的listen socket<连接请求三次握手后，往我这里扔，我去通过accept 得到  连接的socket>，连接socket<连接后的数据读写使用的> ）
                client.configureBlocking(false);
                System.out.println("client...port: " + client.socket().getPort());
                clients.add(client);
            }

            // 在堆外申请缓存内存空间(也可以在堆内)
            ByteBuffer buffer = ByteBuffer.allocateDirect(4096);

            // 2、遍历已经链接进来的客户端能不能读写数据
            for (SocketChannel c : clients) {
                /**
                 * 接收数据也不会阻塞
                 *  >0: 表示获取到客户端发送的数据
                 *  -1: 表示端口连接
                 *  0: 表示没有数据发送过来
                 */
                int num = c.read(buffer);
                if (num > 0) {
                    buffer.flip();
                    byte[] bytes = new byte[buffer.limit()];
                    buffer.get(bytes);

                    String data = new String(bytes);
                    System.out.println(c.socket().getPort() + " : " + data);
                    buffer.clear();
                }
            }
        }
    }
}

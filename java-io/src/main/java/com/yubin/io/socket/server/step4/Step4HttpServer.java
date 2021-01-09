package com.yubin.io.socket.server.step4;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * 简单的Http服务之NIO模型
 *
 * @author YUBIN
 * @create 2021-01-06
 */
public class Step4HttpServer {

    private ServerSocketChannel ssc;

    public void listen(int port) throws IOException {
        ssc = ServerSocketChannel.open();
        ssc.bind(new InetSocketAddress(port));
        // Reactive / Reactor
        ssc.configureBlocking(false);

        Selector selector = Selector.open();
        ssc.register(selector, ssc.validOps(), null);

        ByteBuffer buffer = ByteBuffer.allocate(1024 * 16);

        for (; ; ) {
            // NonBlocking
            int numOfKeys = selector.select();
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                if (key.isAcceptable()) {
                    SocketChannel channel = ssc.accept();
                    if (channel == null) {
                        continue;
                    }
                    channel.configureBlocking(false);
                    channel.register(selector, SelectionKey.OP_READ);
                } else {
                    SocketChannel channel = (SocketChannel) key.channel();
                    buffer.clear();
                    channel.read(buffer);
                    String request = new String(buffer.array());
                    buffer.clear();
                    buffer.put("HTTP/1.1 200 ok\n\nHello NIO!".getBytes());
                    buffer.flip();
                    channel.write(buffer);
                    channel.close(); // 相当于socket.close();
                }
            }
        }
    }
}

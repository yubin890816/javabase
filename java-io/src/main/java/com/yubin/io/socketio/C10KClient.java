package com.yubin.io.socketio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;

/**
 * C10K问题
 *
 * @author YUBIN
 * @create 2021-01-02
 */
public class C10KClient {

    public static void main(String[] args) {
        LinkedList<SocketChannel> clients = new LinkedList<>();
        // 要连的服务端ip: 192.168.254.3(baseOS)、port: 9090
        InetSocketAddress serverAddr = new InetSocketAddress("192.168.254.3", 9090);

        //端口号的问题：65535
        //  windows
        for (int i = 10000; i < 65000; i++) { // 55000 * 2
            try {
                SocketChannel client1 = SocketChannel.open();

                SocketChannel client2 = SocketChannel.open();

                /**
                 * linux中你看到的连接就是：
                 * client...port: 10508
                 * client...port: 10508
                 */
                // 对应VMware的ip地址
                client1.bind(new InetSocketAddress("192.168.254.1", i)); // 客户端1
                //  192.168.254.1:10000   192.168.254.3:9090
                client1.connect(serverAddr);
                clients.add(client1);

                // 对应无线网连接的ip地址
                /*client2.bind(new InetSocketAddress("10.103.231.102", i)); // 客户端2
                //  10.103.231.102:10000  192.168.254.3:9090
                client2.connect(serverAddr);
                clients.add(client2);*/

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("clients "+ clients.size());

        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /*public static void main(String[] args) {
        LinkedList<SocketChannel> clients = new LinkedList<>();
        // 要连的服务端ip: 192.168.254.3(baseOS)、port: 9090
        InetSocketAddress serverAddr = new InetSocketAddress("192.168.254.3", 9090);

        // 端口号的问题: 65535
        for (int i = 10000; i < 65000; i++) { // 55000 * 2
            try {
                SocketChannel client1 = SocketChannel.open(); // 客户端1

                SocketChannel client2 = SocketChannel.open(); // 客户端2
                // 对应VMware的ip地址
                client1.bind(new InetSocketAddress("192.168.254.1", i));
                client1.connect(serverAddr);
                clients.add(client1);
                // 对应无线网连接的ip地址
                client2.bind(new InetSocketAddress("10.103.231.102", i));
                client2.connect(serverAddr);
                clients.add(client2);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("clients " + clients.size());
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
}

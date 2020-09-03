package com.yubin.socket.server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * udp协议服务端
 *
 * @author YUBIN
 * @create 2020-09-03
 */
public class UDPServer {

    public static void main(String[] args) throws Exception {
        DatagramSocket datagramSocket = new DatagramSocket(10001);
        byte[] buffer = new byte[1024];
        // 用来接收传递过来的数据
        DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length);
        // 利用创建号的数据报包对象来接收数据
        datagramSocket.receive(datagramPacket);
        // 打印输出信息
        System.out.println(new String(datagramPacket.getData(), 0, datagramPacket.getLength()));
        // 释放资源
        datagramSocket.close();
    }
}

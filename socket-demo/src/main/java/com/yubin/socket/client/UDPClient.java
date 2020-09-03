package com.yubin.socket.client;

import java.net.*;
import java.util.Scanner;

/**
 * UDP协议客户端
 *
 * @author YUBIN
 * @create 2020-09-03
 */
public class UDPClient {

    public static void main(String[] args) throws Exception {
        // 创建UDP通信的Socket
        DatagramSocket datagramSocket = new DatagramSocket(10000);
        // 从控制台读取数据
        Scanner scanner = new Scanner(System.in);
        String str = scanner.nextLine();
        // 创建数据报包对象
        DatagramPacket datagramPacket = new DatagramPacket(str.getBytes(), str.getBytes().length, InetAddress.getByName("localhost"), 10001);
        // 发送数据
        datagramSocket.send(datagramPacket);
        // 释放资源
        datagramSocket.close();
    }
}

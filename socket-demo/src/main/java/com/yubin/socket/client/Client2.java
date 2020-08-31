package com.yubin.socket.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * TCP客户端同时能接收服务端返回的数据
 *      客户端向服务端发送数据
 * @author YUBIN
 * @create 2020-09-01
 */
public class Client2 {

    public static void main(String[] args) throws IOException {
        // 创建socket对象,其实是开启实现io的虚拟接口(此接口不是java中的接口,而是类似于网线的插槽)
        // 需要指定数据接收方的ip地址和端口号
        Socket client = new Socket("localhost", 10000);
        // ------------------------向外进行输出-----------------------
        // 获取输出流对象,向服务端发送数据
        OutputStream outputStream = client.getOutputStream();
        // 数据输出
        outputStream.write("hello java".getBytes());

        // -------------------------接收服务端返回的数据----------------
        // 获取输入流对象
        InputStream inputStream = client.getInputStream();
        byte[] buffer = new byte[1024];
        int length = inputStream.read(buffer);
        System.out.println("服务端响应的数据是: " + new String(buffer, 0, length));

        // 关闭流操作
        inputStream.close();
        outputStream.close();
        client.close();
    }
}

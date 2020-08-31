package com.yubin.socket.client;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * TCP客户端
 *      客户端向服务端发送数据
 * @author YUBIN
 * @create 2020-09-01
 */
public class Client {

    public static void main(String[] args) throws IOException {
        // 创建socket对象,其实是开启实现io的虚拟接口(此接口不是java中的接口,而是类似于网线的插槽)
        // 需要指定数据接收方的ip地址和端口号
        Socket client = new Socket("localhost", 10086);
        // 获取输出流对象,向服务端发送数据
        OutputStream outputStream = client.getOutputStream();
        // 将输出流对象进行包装(方便发送数据)
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
        // 传输数据
        dataOutputStream.writeUTF("hello,你好");
        // 关闭流
        dataOutputStream.close();
        outputStream.close();
        client.close();
    }
}

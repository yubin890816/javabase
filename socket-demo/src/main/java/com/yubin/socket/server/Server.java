package com.yubin.socket.server;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * TCP服务端
 *
 * @author YUBIN
 * @create 2020-09-01
 */
public class Server {

    public static void main(String[] args) throws IOException {

        // 服务端需要使用ServerSocket来开放本地的端口
        ServerSocket serverSocket = new ServerSocket(10086);
        // 需要接收client传输过来的数据,需要定义socket对象
        Socket server = serverSocket.accept();
        // 通过Server获取输入流对象
        InputStream inputStream = server.getInputStream();
        // 对象输入流做包装,包装成DataInputStream
        DataInputStream dataInputStream = new DataInputStream(inputStream);
        // 接收数据
        System.out.println(dataInputStream.readUTF());
        // 关闭流
        dataInputStream.close();
        inputStream.close();
        server.close();
        serverSocket.close();
    }
}

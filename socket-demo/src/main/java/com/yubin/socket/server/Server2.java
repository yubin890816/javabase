package com.yubin.socket.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * TCP服务端
 *
 * @author YUBIN
 * @create 2020-09-01
 */
public class Server2 {

    public static void main(String[] args) throws IOException {

        // 服务端需要使用ServerSocket来开放本地的端口
        ServerSocket serverSocket = new ServerSocket(10000);
        // 需要接收client传输过来的数据,需要定义socket对象
        Socket server = serverSocket.accept();
        // -------------------------接收服务端返回的数据----------------
        // 通过Server获取输入流对象
        InputStream inputStream = server.getInputStream();
        byte[] buffer = new byte[1024];
        int length = inputStream.read(buffer);
        System.out.println("客户端传输的数据是: " + new String(buffer, 0, length));

        // -------------------------返回客户端数据---------------------
        OutputStream outputStream = server.getOutputStream();
        outputStream.write("你好,收到".getBytes());

        // 关闭流
        outputStream.close();
        inputStream.close();
        server.close();
        serverSocket.close();
    }
}

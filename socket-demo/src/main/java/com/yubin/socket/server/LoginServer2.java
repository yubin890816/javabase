package com.yubin.socket.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 登录服务端
 *
 * @author YUBIN
 * @create 2020-09-03
 */
public class LoginServer2 {

    public static void main(String[] args) throws IOException {
        // 创建服务端对象,开放10086端口
        ServerSocket serverSocket = new ServerSocket(10086);
        while (true) {
            // 获取服务端Socket
            Socket socket = serverSocket.accept();
            new Thread(new LoginThread(socket)).start();
        }
    }
}

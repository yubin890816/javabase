package com.yubin.socket.server;

import com.yubin.socket.client.User;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 登录服务端
 *
 * @author YUBIN
 * @create 2020-09-03
 */
public class LoginServer {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        // 创建服务端对象,开放10086端口
        ServerSocket serverSocket = new ServerSocket(10086);
        while (true) {
            // 获取服务端Socket
            Socket socket = serverSocket.accept();
            // 获取输入流
            InputStream inputStream = socket.getInputStream();
            // 创建ObjectInputStream
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            User user = (User) objectInputStream.readObject();
            String result = "";
            if ("zhangsan".equals(user.getUserName()) && "123456".equals(user.getPassword())) {
                System.out.println("欢迎您:" + user.getUserName());
                result = "登陆成功";
            } else {
                result = "登录失败";
            }
            // 截断输入流
            socket.shutdownInput();

            // 告知客户端登录结果
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(result.getBytes());
            // 截断输出流
            socket.shutdownOutput();

            // 释放资源
            outputStream.close();
            objectInputStream.close();
            inputStream.close();
            socket.close();
        }
    }
}

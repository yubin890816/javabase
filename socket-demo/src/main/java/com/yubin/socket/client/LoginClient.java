package com.yubin.socket.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * 登录案例客户端
 *
 * @author YUBIN
 * @create 2020-09-03
 */
public class LoginClient {

    public static void main(String[] args) throws IOException {
        // 创建Socket对象
        Socket client = new Socket("localhost", 10086);

        // 获取输出流对象
        OutputStream outputStream = client.getOutputStream();
        // 创建Usr对象
        User user = getUser();

        // 传输对象使用ObjectOutputStream
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(user);
        // 截断输出流
        client.shutdownOutput();

        // 接收服务端响应
        InputStream inputStream = client.getInputStream();
        byte[] buffer = new byte[1024]; // 此处由于知道响应内容少于1024字节
        int length = inputStream.read(buffer);
        String result = new String(buffer, 0, length);
        System.out.println(result);
        // 截断输入流
        client.shutdownInput();

        // 释放资源
        inputStream.close();
        objectOutputStream.close();
        outputStream.close();
        client.close();
    }

    private static User getUser() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入用户名:");
        String userName = scanner.nextLine();
        System.out.println("请输入密码:");
        String password = scanner.nextLine();
        User user = new User();
        user.setUserName(userName);
        user.setPassword(password);
        return user;
    }
}

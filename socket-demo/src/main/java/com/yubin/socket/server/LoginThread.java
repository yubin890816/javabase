package com.yubin.socket.server;

import com.yubin.socket.client.User;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * 登录业务的线程类
 *
 * @author YUBIN
 * @create 2020-09-03
 */
public class LoginThread implements Runnable {

    private Socket socket;

    public LoginThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        InputStream inputStream = null;
        ObjectInputStream objectInputStream = null;
        OutputStream outputStream = null;
        try {
            // 获取输入流
            inputStream = socket.getInputStream();
            // 创建ObjectInputStream
            objectInputStream = new ObjectInputStream(inputStream);
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
            outputStream = socket.getOutputStream();
            outputStream.write(result.getBytes());
            // 截断输出流
            socket.shutdownOutput();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally { // 释放资源
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (objectInputStream != null) {

                try {
                    objectInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

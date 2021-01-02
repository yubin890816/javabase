package com.yubin.io.socketio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author YUBIN
 * @create 2020-10-31
 */
public class TestSocket {

    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(8090); // 在TCP层面相当于起了一个监听
        System.out.println("step1: new ServerSocket(8090)");

        while (true) {
            Socket client = server.accept(); // 接收客户端连接是一个阻塞
            System.out.println("step2: client\t" + client.getPort());

            new Thread(new Runnable() {
                Socket ss;

                public Runnable setSS(Socket s) {
                    ss = s;
                    return this;
                }

                @Override
                public void run() {
                    try {
                        InputStream in = ss.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                        while (true) {
                            System.out.println(reader.readLine()); // 读取客户端输入流是一个阻塞
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }.setSS(client)).start();
        }
    }
}

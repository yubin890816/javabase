package com.yubin.io.socketio;

import java.io.*;
import java.net.Socket;

/**
 * Socket客户端
 *
 * @author YUBIN
 * @create 2020-10-31
 */
public class SocketClient {

    public static void main(String[] args) {
        try {
            Socket client = new Socket("192.168.254.3", 9090);

            client.setSendBufferSize(20); // 发送缓冲区是20字节
            client.setTcpNoDelay(false);
            client.setOOBInline(false);

            OutputStream out = client.getOutputStream();
            InputStream in = System.in;
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            while (true) {
                String line = reader.readLine();
                if (line != null) {
                    byte[] bytes = line.getBytes();
                    for (byte b : bytes) {
                        out.write(b); // 注意这个输出流我是没有flush的
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

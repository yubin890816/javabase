package com.yubin.socket.client;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * 传输图片客户端
 *
 * @author YUBIN
 * @create 2020-09-02
 */
public class PicClient {

    public static void main(String[] args) throws IOException {
        // 创建图片的输入流对象
        InputStream inputStream = new FileInputStream("img.jpg");
        // 创建Socket对象
        Socket client = new Socket("localhost", 10086);
        // 获取输出流对象
        OutputStream outputStream = client.getOutputStream();

        int read = 0;
        while ((read = inputStream.read()) != -1) {
            outputStream.write(read);
        }
        // 添加流输出完成标识
        client.shutdownOutput();
        // 接收服务端的响应
        InputStream inStream = client.getInputStream();
        byte[] buffer = new byte[1024];
        int length = inStream.read(buffer);
        System.out.println(new String(buffer, 0, length));

        // 关闭流
        inStream.close();
        outputStream.close();
        inputStream.close();
        client.close();
    }
}

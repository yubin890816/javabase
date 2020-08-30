package com.yubin.io.otherstream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * ByteArrayInputStream和ByteArrayOutputStream使用演示
 *
 * @author YUBIN
 * @create 2020-08-30
 */
public class ByteArrayStreamDemo {

    public static void main(String[] args) {
        // 创建输入流
        ByteArrayInputStream byteArrayInputStream = null;
        // 创建输出流
        ByteArrayOutputStream byteArrayOutputStream = null;


        try {
            // 处理输出流
            byteArrayOutputStream = new ByteArrayOutputStream();
            byteArrayOutputStream.write("https://www.baidu.com".getBytes());
            byteArrayOutputStream.write("\r\nhttps://spring.io".getBytes());

            // 处理输入流, 输入流的缓冲区数据从输出流获取
            byte[] buffer = byteArrayOutputStream.toByteArray();
            byteArrayInputStream = new ByteArrayInputStream(buffer);
            int read = 0;
            while ((read = byteArrayInputStream.read()) != -1) {
                System.out.print((char)read);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 释放资源
            if (byteArrayInputStream != null) {
                try {
                    byteArrayInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (byteArrayOutputStream != null) {
                try {
                    byteArrayOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

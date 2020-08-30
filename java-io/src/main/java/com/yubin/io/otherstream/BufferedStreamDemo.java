package com.yubin.io.otherstream;

import java.io.*;

/**
 * 缓存区输入输出流的案例演示
 *
 * @author YUBIN
 * @create 2020-08-30
 */
public class BufferedStreamDemo {

    public static void main(String[] args) {

        // 定义目的数据文件
        File dest = new File("abc.txt");
        // 定义目标数据文件
        File src = new File("abc.txt");

        // 创建字节输出流
        OutputStream outputStream = null;
        // 创建缓冲区字节输出流
        BufferedOutputStream bufferedOutputStream = null;

        // 创建字节输入流
        InputStream inputStream = null;
        // 创建缓冲区字节输入流
        BufferedInputStream bufferedInputStream = null;

        try {
            // 处理输出流
            outputStream = new FileOutputStream(dest);
            bufferedOutputStream = new BufferedOutputStream(outputStream);
            bufferedOutputStream.write("https://www.baidu.com".getBytes());
            bufferedOutputStream.write("\r\nhttps://spring.io".getBytes());
            // 刷新缓冲区
            bufferedOutputStream.flush();

            // 处理输入流
            inputStream = new FileInputStream(src);
            bufferedInputStream = new BufferedInputStream(inputStream);
            int read = 0;
            while ((read = bufferedInputStream.read()) != -1) {
                System.out.print((char)read);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 释放资源
            if (bufferedInputStream != null) {
                try {
                    bufferedInputStream.close();
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
            if (bufferedOutputStream != null) {
                try {
                    bufferedOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

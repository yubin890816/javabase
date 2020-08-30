package com.yubin.io.stream;

import java.io.*;

/**
 * 字节输出流基本使用演示
 *
 * @author YUBIN
 * @create 2020-08-30
 */
public class OutputStreamDemo1 {

    public static void main(String[] args) {
        File file = new File("aaa.txt");
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
            // 将指定的字节写入此输出流。 每次写入一个字节
            outputStream.write(99);
            // 将 b.length字节从指定的字节数组写入此输出流。
            outputStream.write("\r\n俞斌是帅哥".getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {

            if (outputStream != null) {
                try {
                    // 关闭输出流
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

package com.yubin.io.stream;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * I/O流
 *    read(byte[] b, int off, int len) :从输入流读取最多 len字节的数据到一个字节数组。
 *    b:读取数据的缓冲区。
 *    off :写入缓冲区 b中的起始偏移量。
 *    len: 要读取的最大字节数
 *
 * @author YUBIN
 * @create 2020-08-30
 */
public class InputStreamDemo4 {

    public static void main(String[] args) {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream("abc.txt");
            // 这个length表示读取到的长度
            int length = 0;
            // 添加缓冲区
            byte[] buffer = new byte[10];
            // while循环的方式遍历文件中的数据
            while ((length = inputStream.read(buffer, 1, 5)) != -1) {
                System.out.println(new String(buffer, 1, length));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

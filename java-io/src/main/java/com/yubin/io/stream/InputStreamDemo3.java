package com.yubin.io.stream;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * I/O流
 *    添加缓冲区的方式进行读取,每次会将数据添加到缓冲区中,当缓冲区满了之后,一次读取,而不是每一个字节进行读取
 *    read(byte[] b) 从输入流读取一些字节数，并将它们存储到缓冲区 b 。 如果读取到文件末尾了则返回的是-1
 *
 * @author YUBIN
 * @create 2020-08-30
 */
public class InputStreamDemo3 {

    public static void main(String[] args) {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream("abc.txt");
            // 这个length表示读取到的长度
            int length = 0;
            // 添加缓冲区
            byte[] buffer = new byte[10];
            // while循环的方式遍历文件中的数据
            while ((length = inputStream.read(buffer)) != -1) {
                System.out.println(new String(buffer, 0, length));
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

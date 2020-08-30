package com.yubin.io.stream;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * I/O流
 *    演示用while循环的方式遍历文件中的内容
 * @author YUBIN
 * @create 2020-08-30
 */
public class InputStreamDemo2 {

    public static void main(String[] args) {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream("abc.txt");
            // 从输入流读取数据的下一个字节
            int read = 0;
            // while循环的方式遍历文件中的数据
            while ((read = inputStream.read()) != -1) {
                System.out.println((char) read);
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

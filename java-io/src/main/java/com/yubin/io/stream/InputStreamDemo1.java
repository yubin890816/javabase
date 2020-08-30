package com.yubin.io.stream;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * I/O流
 * 在Java中需要读写文件中的数据的话,需要使用流的概念
 *  流表示从一个文件将数据返送到另一个文件,包含一个流向的问题
 *      最终需要选择一个参照物: 以当前程序作为参照物
 *      从一个文件中读取数据到程序叫做输入流
 *      从程序输出数据到另一个文件叫做输出流
 *
 * 注意: 当编写IO流程序的时候一定要注意关闭流
 *   步骤:
 *      (1)、选择合适的IO流对象
 *      (2)、创建对象
 *      (3)、传输数据
 *      (4)、关闭流对象(linux默认一个进程最多能打开1024个文件, 会占用系统资源)
 * @author YUBIN
 * @create 2020-08-30
 */
public class InputStreamDemo1 {

    public static void main(String[] args) {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream("abc.txt");
            // 从输入流读取数据的下一个字节
            int read = inputStream.read();
            System.out.println((char) read);
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

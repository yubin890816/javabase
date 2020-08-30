package com.yubin.io.readerOrwriter;

import java.io.*;

/**
 * FileReader案例
 *  在前面的字节输入流中,如果遇到汉字读取出来是一个乱码,如何解决这个问题呢?可以采用Reader字符输入流来处理
 *  字符流可以直接读取中文汉字,而字节流在处理的时候会出现中文乱码
 * @author YUBIN
 * @create 2020-08-30
 */
public class FileReaderDemo1 {

    public static void main(String[] args) {
        File file = new File("abc.txt");
        Reader reader = null;
        try {
            reader = new FileReader(file);
            // 读取一个字符
            int read = reader.read();
            System.out.println((char)read);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    // 关闭字符输入流
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

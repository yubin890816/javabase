package com.yubin.io.readerOrwriter;

import java.io.*;

/**
 * FileReader案例
 *  使用缓冲区读取文件
 * @author YUBIN
 * @create 2020-08-30
 */
public class FileReaderDemo3 {

    public static void main(String[] args) {
        File file = new File("abc.txt");
        Reader reader = null;
        try {
            reader = new FileReader(file);
            // 添加缓冲区
            char[] buffer = new char[10];
            int len = 0;
            while ((len = reader.read(buffer)) != -1) {
                System.out.println(new String(buffer, 0, len));
            }
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

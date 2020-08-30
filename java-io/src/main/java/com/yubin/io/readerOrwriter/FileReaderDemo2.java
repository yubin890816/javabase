package com.yubin.io.readerOrwriter;

import java.io.*;

/**
 * FileReader案例
 *  使用while循环遍历文件文件
 * @author YUBIN
 * @create 2020-08-30
 */
public class FileReaderDemo2 {

    public static void main(String[] args) {
        File file = new File("abc.txt");
        Reader reader = null;
        try {
            reader = new FileReader(file);
            // 读取一个字符
            int read = 0;
            while ((read = reader.read()) != -1) {
                System.out.println((char)read);
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

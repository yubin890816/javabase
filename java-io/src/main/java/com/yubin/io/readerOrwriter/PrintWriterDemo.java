package com.yubin.io.readerOrwriter;

import java.io.*;

/**
 * 将对象的格式表示打印到文本输出流(无需使用flush)
 *
 * @author YUBIN
 * @create 2020-08-31
 */
public class PrintWriterDemo {

    public static void main(String[] args) {
        OutputStream outputStream = null;

        PrintWriter printWriter = null;
        try {
            outputStream = new FileOutputStream("111.txt");
            // 输出到文件
            //printWriter = new PrintWriter(outputStream);
            // 输出到控制台
            printWriter = new PrintWriter(System.out);
            // 输出布尔值
            printWriter.println(true);
            // 输出字符串
            printWriter.println("今天天气真好");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            // 释放资源
            if (printWriter != null) {
                printWriter.close();
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

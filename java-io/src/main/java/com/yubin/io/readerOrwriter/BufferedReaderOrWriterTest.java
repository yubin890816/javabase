package com.yubin.io.readerOrwriter;

import java.io.*;

/**
 * BufferedReader和BufferedWriter测试类
 *
 * @author YUBIN
 * @create 2020-08-31
 */
public class BufferedReaderOrWriterTest {

    public static void main(String[] args) {
        // 创建字符输入流
        Writer writer = null;
        // 创建BufferedWriter输出流
        BufferedWriter bufferedWriter = null;

        // 创建字符输入流
        Reader reader = null;
        // 创建BufferedReader输入流
        BufferedReader bufferedReader = null;
        try {
            // 处理输出流
            writer = new FileWriter("111.txt");
            bufferedWriter = new BufferedWriter(writer);
            bufferedWriter.write("今天我学习了6小时");
            // 换行
            bufferedWriter.newLine();
            bufferedWriter.write("学习贵在坚持和耐心");
            bufferedWriter.flush();

            // 处理输入流
            reader = new FileReader("111.txt");
            bufferedReader = new BufferedReader(reader);
            String result = null;
            while ((result = bufferedReader.readLine()) != null) {
                System.out.println(result);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 释放资源
            if (bufferedWriter != null) {
                try {
                    bufferedWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

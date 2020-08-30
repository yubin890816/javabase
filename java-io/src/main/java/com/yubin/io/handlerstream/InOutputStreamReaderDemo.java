package com.yubin.io.handlerstream;

import java.io.*;

/**
 * 字符输入输出流 处理流
 *
 * @author YUBIN
 * @create 2020-08-30
 */
public class InOutputStreamReaderDemo {

    public static void main(String[] args) {

        // 创建字符输入流
        InputStreamReader inputStreamReader = null;

        // 创建字符输出流
        OutputStreamWriter outputStreamWriter = null;

        // 创建字节输入流
        InputStream inputStream = null;

        // 创建字节输出流
        OutputStream outputStream = null;

        try {
            // 输出流处理
            outputStream = new FileOutputStream("abc.txt");
            outputStreamWriter = new OutputStreamWriter(outputStream, "utf-8");
            outputStreamWriter.write(123);
            outputStreamWriter.write("\r\nabcdefghijk");
            outputStreamWriter.write("\r\n我怎么这么好看");
            // 此处不flush, 则内容还没有被写入到文件中
            outputStreamWriter.flush();
            // 输入流处理
            inputStream = new FileInputStream("abc.txt");
            inputStreamReader = new InputStreamReader(inputStream);
            int read = 0;
            while ((read = inputStreamReader.read()) != -1) {
                System.out.print((char)read);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 释放资源, 关闭资源的时候建议从外层开始关闭
            if (outputStreamWriter != null) {
                try {
                    outputStreamWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStreamReader != null) {
                try {
                    inputStreamReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
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

package com.yubin.io.readerOrwriter;

import java.io.CharArrayReader;
import java.io.CharArrayWriter;
import java.io.IOException;

/**
 * 带缓冲区的字符输入输出流，其不直接操作文件
 *
 * @author YUBIN
 * @create 2020-08-31
 */
public class CharArrayReaderOrWriterTest {

    public static void main(String[] args) {
        // 创建带缓冲区的字符输入流
        CharArrayReader charArrayReader = null;

        // 创建带缓冲区的字符输出流
        CharArrayWriter charArrayWriter = null;
        try {
            // 处理输出流
            charArrayWriter = new CharArrayWriter();
            charArrayWriter.write("我怎么这么好看");
            System.out.println(charArrayWriter.toString());

            // 处理输入流
            charArrayReader = new CharArrayReader(charArrayWriter.toCharArray());
            int read = 0;
            while ((read = charArrayReader.read()) != -1) {
                System.out.print((char)read);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 释放资源
            charArrayReader.close();
            charArrayWriter.close();
        }
    }
}

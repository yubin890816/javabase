package com.yubin.io.readerOrwriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

/**
 * 字符输出流基本案例演示
 *
 * @author YUBIN
 * @create 2020-08-30
 */
public class FileWriterDemo1 {

    public static void main(String[] args) {
        // 定义目的数据文件
        File dest = new File("writer.txt");
        // 创建字符输出流对象
        Writer writer = null;

        try {
            writer = new FileWriter(dest);

            writer.write("www.baidu.com");
            writer.write("\r\n百度一下你就知道");
            /**
             * 什么时候需要加flush方法,什么时候不加flush方法
             *  最保险的方式,在输出流关闭之前每次都flush一下,然后再关闭
             *  当某一个输出流对象内部带有缓冲区的时候,就需要进行flush, 不建议大家去记住每个输出流的分类
             */
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
    }
}

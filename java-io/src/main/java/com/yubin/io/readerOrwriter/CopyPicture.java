package com.yubin.io.readerOrwriter;

import java.io.*;

/**
 * 复制图片
 *
 * @author YUBIN
 * @create 2020-08-30
 */
public class CopyPicture {

    public static void main(String[] args) {
        // 定义目标数据文件
        File src = new File("1.jpg");
        // 定义目的数据文件
        File dest = new File("2.jpg");

        // 创建字节输入流
        InputStream inputStream = null;
        // 创建字节输出流
        OutputStream outputStream = null;

        try {
            inputStream = new FileInputStream(src);
            outputStream = new FileOutputStream(dest);

            // 通过缓冲区读取文件
            byte[] buffer = new byte[1024];
            // 每次读取的文件字节个数
            int len = 0;
            // 循环遍历读取文件
            while ((len = inputStream.read(buffer)) != -1) {
                // 写入输出流
                outputStream.write(buffer, 0, len);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭输入流
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // 关闭输出流
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

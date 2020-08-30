package com.yubin.io.stream;

import java.io.*;

/**
 * 文件拷贝案例演示
 *
 * @author YUBIN
 * @create 2020-08-30
 */
public class CopyFile {

    public static void main(String[] args) {
        // 定义源数据文件
        File src = new File("abc.txt");
        // 定义目的数据文件
        File dest = new File("aaa.txt");

        // 创建输入流对象
        InputStream inputStream = null;
        // 创建输出流对象
        OutputStream outputStream = null;
        try {
            inputStream = new FileInputStream(src);
            outputStream = new FileOutputStream(dest);

            // 带缓冲区的输入输出方式
            byte[] buffer = new byte[1024];
            // 每次读取的字节长度
            int len = 0;
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
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

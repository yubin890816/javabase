package com.yubin.io.otherstream;

import java.io.*;

/**
 * DataXxxputStream案例演示
 *
 * @author YUBIN
 * @create 2020-08-30
 */
public class DataStreamDemo {

    public static void main(String[] args) {
        // 定义目的数据文件
        File dest = new File("abc.txt");
        // 定义目标数据文件
        File src = new File("abc.txt");

        // 创建字节输出流
        OutputStream outputStream = null;
        // 创建数据输出流
        DataOutputStream dataOutputStream = null;
        // 创建字节输入流
        InputStream inputStream = null;
        // 创建数据输入流
        DataInputStream dataInputStream = null;
        try {
            // 处理输出流
            outputStream = new FileOutputStream(dest);
            dataOutputStream = new DataOutputStream(outputStream);
            dataOutputStream.writeBoolean(true);
            dataOutputStream.writeInt(12);
            dataOutputStream.writeDouble(3.14);
            dataOutputStream.writeUTF("我怎么这么好看");
            dataOutputStream.flush();

            // 处理输入流
            inputStream = new FileInputStream(src);
            dataInputStream = new DataInputStream(inputStream);
            // 需要注意的是,读取的顺序要与写入的顺序一致
            // 读取boolean数据
            System.out.println(dataInputStream.readBoolean());
            // 读取int数据
            System.out.println(dataInputStream.readInt());
            // 读取double数据
            System.out.println(dataInputStream.readDouble());
            // 读取字符串数据
            System.out.println(dataInputStream.readUTF());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 释放资源
            if (dataInputStream != null) {
                try {
                    dataInputStream.close();
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
            if (dataOutputStream != null) {
                try {
                    dataOutputStream.close();
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

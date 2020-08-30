package com.yubin.io.otherstream;

import java.io.*;

/**
 * 对象序列化和反序列化输入输出流案例演示
 *
 * @author YUBIN
 * @create 2020-08-30
 */
public class ObjectStreamDemo {

    public static void main(String[] args) {
        // 定义目的数据文件
        File dest = new File("abc.txt");
        // 定义目标数据文件
        File src = new File("abc.txt");

        // 创建字节输出流
        OutputStream outputStream = null;
        // 创建数据输出流
        ObjectOutputStream objectOutputStream = null;
        // 创建字节输入流
        InputStream inputStream = null;
        // 创建数据输入流
        ObjectInputStream objectInputStream = null;

        try {
            // 处理输出流
            outputStream = new FileOutputStream(dest);
            objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeUTF("我帅不帅跟你有关系么");
            objectOutputStream.writeObject(new Person(1, "帅哥", "123456"));
            objectOutputStream.flush();

            // 处理输入流
            inputStream = new FileInputStream(src);
            objectInputStream = new ObjectInputStream(inputStream);
            System.out.println(objectInputStream.readUTF());
            System.out.println(objectInputStream.readObject());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            // 释放资源
            if (objectInputStream != null) {
                try {
                    objectInputStream.close();
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
            if (objectOutputStream != null) {
                try {
                    objectOutputStream.close();
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

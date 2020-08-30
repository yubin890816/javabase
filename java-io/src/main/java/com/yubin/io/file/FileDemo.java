package com.yubin.io.file;

import java.io.File;
import java.io.IOException;

/**
 * java中的File类
 *
 * @author YUBIN
 * @create 2020-08-30
 */
public class FileDemo {

    /**
     * File提供了对当前文件系统中文件的部分操作
     */
    public static void main(String[] args) throws IOException {
        method1();
    }

    /**
     * File类的基本使用,及常用方法介绍
     * @throws IOException
     */
    private static void method1() throws IOException {
        //File file = new File("abc.txt");
        File file = new File("src/abc.txt");

        // 创建文件
        file.createNewFile();

        // 判断文件的属性, 都会返回boolean类型的值
        System.out.println("判断当前文件是否可执行:" + file.canExecute());
        System.out.println("判断当前文件是否可读:" + file.canRead());
        System.out.println("判断当前文件是否可写:" + file.canWrite());

        // 判断当前文件是否存在
        System.out.println("判断当前文件是否存在:" + file.exists());

        // 获取文件的名称
        System.out.println("获取当前文件的名称:" + file.getName());
        System.out.println("获取当前文件的绝对路径:" + file.getAbsolutePath());
        // 获取当前文件的父路径名称,如果当前文件的路径中只包含文件名称,则显示空
        System.out.println("获取当前文件的父路径名称:" + file.getParent());

        // 返回文件绝对路径的规范格式
        System.out.println("返回文件绝对路径的规范格式:" + file.getCanonicalPath());
        // 获取当前操作系统的文件分隔符
        System.out.println("获取当前操作系统的文件分隔符:" + File.separator);

        // 无论当前文件是否存在,只要给定具体的路径,都可以返回响应的路径名称
        File file2 = new File("d:/yubin.txt");
        System.out.println(file2.getAbsolutePath());

        // 判断当前文件是文件还是目录(需要当前文件存在,否则都返回的是false)
        System.out.println("判断当前File是否是目录:" + file2.isDirectory());
        System.out.println("判断当前File是否是文件:" + file2.isFile());

        System.out.println("===============================================");

        File file3 = new File("d:/");
        // 返回一个字符串数组，此抽象路径名表示的目录中的文件和目录。
        String[] list = file3.list();
        for (int i = 0; i < list.length; i++) {
            System.out.println(list[i]);
        }
        System.out.println("===============================================");

        // 返回一个抽象路径名数组，表示由该抽象路径名表示的目录中的文件。
        File[] files = file3.listFiles();
        for (int i = 0; i < files.length; i++) {
            System.out.println(files[i]);
        }

        File file4 = new File("d:/a");
        // 创建单级目录(如果已存在,则返回false)
        System.out.println("创建单级目录:" + file4.mkdir());

        File file5 = new File("d:/a/b/c/d/e/f");
        System.out.println("创建多级目录:" + file5.mkdirs());
    }
}

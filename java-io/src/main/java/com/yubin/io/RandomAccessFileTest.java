package com.yubin.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * 随机读写文件测试类
 *
 * @author YUBIN
 * @create 2020-08-31
 */
public class RandomAccessFileTest {

    public static void main(String[] args) {
        // 定义目标数据文件
        File file = new File("doc.txt");
        // 获取整个文件的大小(字节)
        long length = file.length();
        // 规定块的大小
        int blockSize = 1024;
        // 计算文件可以被切割成多少分
        int size = (int) Math.ceil(length * 1.0 / blockSize);

        // 初始化起始偏移量
        int beginPos = 0;
        // 初始化每次读取的字节数
        int actualSize = 0;
        for (int i = 0; i < size; i++) {
            beginPos = i * blockSize;
            if (i == size - 1) {
                actualSize = (int) length;
            } else {
                actualSize = blockSize;
                length -= actualSize;
            }
            System.out.println(i + "---》起始位置是：" + beginPos + "---->读取的大小是：" + actualSize);
            readSplit(i, beginPos, actualSize);
        }
    }

    private static void readSplit(int i,int beginPos,int actualSize){
        RandomAccessFile randomAccessFile = null;
        try {
            randomAccessFile = new RandomAccessFile(new File("doc.txt"),"r");
            //表示从哪个偏移量开始读取数据
            randomAccessFile.seek(beginPos);
            byte[] bytes = new byte[1024];
            int length = 0;
            while((length = randomAccessFile.read(bytes))!=-1){
                if(actualSize>length){
                    System.out.println(new String(bytes,0,length));
                    actualSize-=length;
                }else{
                    System.out.println(new String(bytes,0,actualSize));
                    break;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                randomAccessFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

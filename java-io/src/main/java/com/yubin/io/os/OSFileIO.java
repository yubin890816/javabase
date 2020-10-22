package com.yubin.io.os;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 测试Linux环境下的IO操作
 *
 * @author YUBIN
 * @create 2020-10-19
 */
public class OSFileIO {

    private static byte[] data = "123456789\n".getBytes();

    private static String path = "/root/testfileio/out.txt";

    public static void main(String[] args) throws Exception {
        switch (args[0]) {
            case "0":
                testBasicFileIO();
                break;
            case "1":
                testBufferedFileIO();
                break;
            case "2" :
                testRandomAccessFileWrite();
            case "3":
                whatByteBuffer();
            default:
        }
    }

    // 最基本的file写 每隔10s往文件中写数据,注意这里没有flush操作
    public static  void testBasicFileIO() throws Exception {
        File file = new File(path);
        FileOutputStream out = new FileOutputStream(file);
        while(true){
            //Thread.sleep(10);
            out.write(data);
        }
    }

    //测试buffer文件IO
    //  jvm  8kB   syscall  write(8KBbyte[]) 这个效率要比普通io快很多
    public static void testBufferedFileIO() throws Exception {
        File file = new File(path);
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
        while(true){
            //Thread.sleep(10);
            out.write(data);
        }
    }



    //测试文件NIO
    public static void testRandomAccessFileWrite() throws  Exception {
        // 创建一个随机访问文件的对象（通道是读写两个方向）, 并调用普通的写
        RandomAccessFile raf = new RandomAccessFile(path, "rw");
        raf.write("hello shanghai\n".getBytes());
        raf.write("hello yubin\n".getBytes());
        System.out.println("write------------");
        System.in.read(); // 程序阻塞

        // 随机写
        raf.seek(4); // 将偏移量置位4
        raf.write("ooxx".getBytes());

        System.out.println("seek---------");
        System.in.read();

        // 对外映射写
        FileChannel rafchannel = raf.getChannel(); // 拿到文件通道
        //mmap  堆外  和文件映射的   byte  not  objtect
        MappedByteBuffer map = rafchannel.map(FileChannel.MapMode.READ_WRITE, 0, 4096);

        map.put("@@@".getBytes());  //不是系统调用  但是数据会到达 内核的pagecache
        //曾经我们是需要out.write()  这样的系统调用，才能让程序的data 进入内核的pagecache
        //换言之就是曾经必须有用户态内核态切换
        //mmap的内存映射，依然是内核的pagecache体系所约束的！！！
        //换言之，丢数据
        //你可以去github上找一些 其他C程序员写的jni扩展库，使用linux内核的Direct IO
        //直接IO是忽略linux的pagecache
        //是把pagecache  交给了程序自己开辟一个字节数组当作pagecache，动用代码逻辑来维护一致性/dirty。。。一系列复杂问题

        System.out.println("map--put--------");
        System.in.read();

        //map.force(); //  flush



        raf.seek(0);

        ByteBuffer buffer = ByteBuffer.allocate(8192);
        //ByteBuffer buffer = ByteBuffer.allocateDirect(1024);

        int read = rafchannel.read(buffer);   // 相当于 buffer.put()
        System.out.println(buffer);
        buffer.flip();
        System.out.println(buffer);

        for (int i = 0; i < buffer.limit(); i++) {
            Thread.sleep(200);
            System.out.print(((char)buffer.get(i)));
        }
    }

    public static void whatByteBuffer(){

        ByteBuffer buffer = ByteBuffer.allocate(1024); // 给ByteBuffer分配1024的大小(还可以使用下面的方式分配,前种是分配在堆上,下一个方式是堆外分配)
        //ByteBuffer buffer = ByteBuffer.allocateDirect(1024);

        System.out.println("postition: " + buffer.position()); // 偏移指针
        System.out.println("limit: " +  buffer.limit()); // 大小限制
        System.out.println("capacity: " + buffer.capacity()); // 总大小
        System.out.println("mark: " + buffer);

        buffer.put("123".getBytes());

        System.out.println("-------------put:123......");
        System.out.println("mark: " + buffer);

        buffer.flip();   //读写行为交替(翻转)

        System.out.println("-------------flip......");
        System.out.println("mark: " + buffer);

        buffer.get(); // 读取一个字节

        System.out.println("-------------get......");
        System.out.println("mark: " + buffer);

        buffer.compact();

        System.out.println("-------------compact......");
        System.out.println("mark: " + buffer);

        buffer.clear(); // 清除

        System.out.println("-------------clear......");
        System.out.println("mark: " + buffer);

    }
}

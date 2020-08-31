package com.yubin.socket;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * InetAddress：此类表示Inetrnet协议(IP)地址
 *  注意此类没有构造方法,通过该类的静态方法获取InetAddress对象
 * @author YUBIN
 * @create 2020-09-01
 */
public class InetAddressDemo {

    public static void main(String[] args) throws UnknownHostException {
        // 获取本地主机的地址
        InetAddress inetAddress1 = InetAddress.getLocalHost();
        // 打印结果: 主机名/ip地址
        System.out.println(inetAddress1);

        InetAddress inetAddress2 = InetAddress.getByName("yubin");
        System.out.println(inetAddress2);

        // 注意不在同一个网段内的主机名无法获取到
        //InetAddress inetAddress3 = InetAddress.getByName("PS20190424MARAE");
        //System.out.println(inetAddress3);

        InetAddress inetAddress3 = InetAddress.getByName("www.baidu.com");
        System.out.println(inetAddress3);

        // 获取主机ip地址
        System.out.println(inetAddress1.getHostAddress());
        // 获取主机名
        System.out.println(inetAddress1.getHostName());


    }
}

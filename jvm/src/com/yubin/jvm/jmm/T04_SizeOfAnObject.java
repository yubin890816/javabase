package com.yubin.jvm.jmm;

import com.yubin.jvm.agent.ObjectSizeAgent;

/**
 * 测试一个类的大小
 *
 * @author YUBIN
 * @create 2020-10-21
 */
public class T04_SizeOfAnObject {

    public static void main(String[] args) {
        System.out.println(ObjectSizeAgent.sizeOf(new Object()));
        System.out.println(ObjectSizeAgent.sizeOf(new int[] {}));
        System.out.println(ObjectSizeAgent.sizeOf(new P()));
    }

    private static class P {
        //8 _markword
        //4 _oop指针
        int id;         //4
        String name;    //4
        int age;        //4

        byte b1;        //1
        byte b2;        //1

        Object o;       //4
        byte b3;        //1

    }
}

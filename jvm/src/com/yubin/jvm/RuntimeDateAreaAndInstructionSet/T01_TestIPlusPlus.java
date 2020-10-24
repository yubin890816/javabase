package com.yubin.jvm.RuntimeDateAreaAndInstructionSet;

/**
 * 测试i++和++i
 *
 * @author YUBIN
 * @create 2020-10-23
 */
public class T01_TestIPlusPlus {

    public static void main(String[] args) {
        int i = 8;
        i = i++; // 运行结果 8
        //i = ++i; // 运行结果9
        System.out.println(i);
    }
}

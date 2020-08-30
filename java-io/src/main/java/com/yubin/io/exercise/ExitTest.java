package com.yubin.io.exercise;

import java.io.*;

/**
 * 测试类 当控制台输入exit时退出程序
 *
 * @author YUBIN
 * @create 2020-08-31
 */
public class ExitTest {

    public static void main(String[] args) {

        try (InputStreamReader inputStreamReader = new InputStreamReader(System.in);
             OutputStreamWriter outputStreamWriter = new OutputStreamWriter(System.out);
             BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader);) {
            String str = "";
            while (!str.equals("exit")) {
                str = bufferedReader.readLine();
                bufferedWriter.write(str);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package com.yubin.io.exercise;

import java.io.*;
import java.net.URL;

/**
 * 读取百度首页的内容
 *
 * @author YUBIN
 * @create 2020-08-31
 */
public class BaiDuTest {

    public static void main(String[] args) {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new URL("https://baidu.com").openStream(), "utf-8"));
             BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("baidu.html"));) {
            String result = null;
            while ((result = bufferedReader.readLine()) != null) {
                bufferedWriter.write(result);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

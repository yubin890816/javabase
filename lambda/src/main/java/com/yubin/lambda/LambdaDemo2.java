package com.yubin.lambda;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Lambda表达式测试类
 *
 * @author YUBIN
 * @create 2020-09-03
 */
public class LambdaDemo2 {

    // 案例,对集合中的元素按照长度进行排序
    public static void main(String[] args) {
        System.out.println("==============集合排序非Lambda表达式的写法===============");
        List<String> list1 = Arrays.asList("java", "javascript", "scala", "python");
        Collections.sort(list1, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.length() - o2.length();
            }
        });
        for (String s : list1) {
            System.out.println(s);
        }


        System.out.println("==============集合排序Lambda表达式的写法===============");
        List<String> list2 = Arrays.asList("java", "javascript", "scala", "python");
        Collections.sort(list2, (o1, o2) -> o1.length() - o2.length()); // 大括号里面只有一行的话,可以省略大括号
        list2.forEach(System.out::println);
    }
}

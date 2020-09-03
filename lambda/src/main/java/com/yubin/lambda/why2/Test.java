package com.yubin.lambda.why2;

import com.yubin.lambda.why.Student;

import java.util.ArrayList;
import java.util.List;

/**
 * 测试类
 *
 * @author YUBIN
 * @create 2020-09-04
 */
public class Test {

    public static void main(String[] args) {
        List<Student> list = new ArrayList<>();
        list.add(new Student("zhangsan", 21, 91));
        list.add(new Student("lisi", 22, 82));
        list.add(new Student("wangwu", 25, 63));
        list.add(new Student("maliu", 24, 78));
        list.add(new Student("zhaoqi", 28, 67));

        // 需求1: 查找年龄大于22的学生
        findByFilter(list, new AgeFilter());
        System.out.println("===============================");
        // 需求2: 查找分数大于75分的学生
        findByFilter(list, new ScoreFilter());
    }

    private static void findByFilter(List<Student> students, StudentFilter filter) {
        List<Student> list = new ArrayList<>();
        for (Student student : students) {
            if (filter.filter(student)) {
                list.add(student);
            }
        }
        for (Student student : list) {
            System.out.println(student);
        }
    }
}

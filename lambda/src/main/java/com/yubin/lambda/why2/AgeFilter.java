package com.yubin.lambda.why2;

import com.yubin.lambda.why.Student;

/**
 * 年龄过滤实现类
 *
 * @author YUBIN
 * @create 2020-09-04
 */
public class AgeFilter implements StudentFilter {

    @Override
    public boolean filter(Student student) {
        return student.getAge() > 22;
    }
}

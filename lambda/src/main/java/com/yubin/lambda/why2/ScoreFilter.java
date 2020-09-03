package com.yubin.lambda.why2;

import com.yubin.lambda.why.Student;

/**
 * 分数过滤类
 *
 * @author YUBIN
 * @create 2020-09-04
 */
public class ScoreFilter implements StudentFilter {
    @Override
    public boolean filter(Student student) {
        return student.getScore() > 75;
    }
}

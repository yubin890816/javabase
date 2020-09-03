package com.yubin.lambda.why2;

import com.yubin.lambda.why.Student;

/**
 * Student过滤接口
 *
 * @author YUBIN
 * @create 2020-09-04
 */
public interface StudentFilter {

    public boolean filter(Student student);
}

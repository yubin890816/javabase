package com.yubin.lambda;

import com.yubin.lambda.why.Student;

/**
 * 自定义的函数式接口
 *
 * @author YUBIN
 * @create 2020-09-04
 */
@FunctionalInterface // 该注解非必须
public interface MyFunctionalInterface {

    void insert(Student student);
}

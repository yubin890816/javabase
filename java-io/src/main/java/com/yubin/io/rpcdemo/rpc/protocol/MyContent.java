package com.yubin.io.rpcdemo.rpc.protocol;

import java.io.Serializable;

/**
 * RPC通信 消息体的封装
 *
 * @author YUBIN
 * @create 2021-01-17
 */
public class MyContent implements Serializable {
    private String name; // 接口全限定名
    private String methodName; // 调用的方法名称
    private Class<?>[] parameterTypes; // 方法参数类型
    private Object[] args; // 参数列表
    private Object res; // 服务端的响应

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public Object getRes() {
        return res;
    }

    public void setRes(Object res) {
        this.res = res;
    }
}

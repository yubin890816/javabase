package com.yubin.io.rpcdemo.service;

/**
 * 服务实现
 *
 * @author YUBIN
 * @create 2021-01-17
 */
public class MyCar implements Car {
    @Override
    public String ooxx(String msg) {
        System.out.println("server get client arg: " + msg);
        return "server return " + msg;
    }
}

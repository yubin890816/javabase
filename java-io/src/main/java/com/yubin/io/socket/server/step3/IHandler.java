package com.yubin.io.socket.server.step3;

import java.io.IOException;

/**
 * 处理http请求的接口
 *
 * @author YUBIN
 * @create 2021-01-05
 */
@FunctionalInterface
public interface IHandler {

    public void handler(Request request, Response response) throws IOException;
}

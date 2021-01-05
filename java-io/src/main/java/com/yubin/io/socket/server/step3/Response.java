package com.yubin.io.socket.server.step3;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * http响应封装类
 *
 * @author YUBIN
 * @create 2021-01-05
 */
public class Response {

    private Socket socket;

    private int status;

    private static Map<Integer, String> codeMap = new HashMap<>();

    static {
        codeMap.put(200, "OK");
    }

    public Response(Socket socket) {
        this.socket = socket;
    }

    public void send(String msg) throws IOException {
        String resp = "HTTP/1.1 " + this.status + " " + this.codeMap.get(this.status) + "\n";
        resp += "\n";
        resp += msg;
        this.sendRaw(resp);
    }

    public void sendRaw(String msg) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        bufferedWriter.write(msg);
        bufferedWriter.flush();
        socket.close();
    }
}

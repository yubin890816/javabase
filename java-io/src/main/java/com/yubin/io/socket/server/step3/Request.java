package com.yubin.io.socket.server.step3;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpParser;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * http请求封装类
 *
 * @author YUBIN
 * @create 2021-01-05
 */
public class Request {
    private static Pattern methodRegex = Pattern.compile("(GET|PUT|POST|DELETE|OPTIONS|TRACE|HEAD)");

    private String body;

    private String method;

    private HashMap<String, String> headers;

    public Request(Socket socket) throws IOException {
        DataInputStream inputStream = new DataInputStream(socket.getInputStream());


        // GET /path HTTP/1.1
        String methodLine = HttpParser.readLine(inputStream, "utf-8");
        Matcher matcher = methodRegex.matcher(methodLine);
        matcher.find();
        String method = matcher.group();

        // Content-Type:xxxx
        // Length:xxx
        Header[] headerArr = HttpParser.parseHeaders(inputStream, "UTF-8");
        HashMap<String, String> headers = new HashMap<>();
        for (Header header : headerArr) {
            headers.put(header.getName(), header.getValue());
        }
        // 读取body
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder body = new StringBuilder();
        char[] buffer = new char[1024];

        String line = "";
        while (inputStream.available() > 0) {
            bufferedReader.read(buffer);
            body.append(buffer);
        }

        this.body = body.toString();
        this.method = method;
        this.headers = headers;
    }

    public HashMap<String, String> getHeaders() {
        return headers;
    }

    public String getMethod() {
        return method;
    }

    public String getBody() {
        return body;
    }
}

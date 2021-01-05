package com.yubin.io.socket.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.function.Function;

/**
 * 简单Http服务之面向对象封装
 *
 * @author YUBIN
 * @create 2021-01-05
 */
public class Step1HttpServer {

    private ServerSocket serverSocket;

    private Function<String, String> handler;

    public Step1HttpServer(Function<String, String> handler) {
        this.handler = handler;
    }

    public void listen(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        while (true) {
            this.accept();
        }
    }

    private void accept() throws IOException {
        try {
            Socket socket = serverSocket.accept();
            System.out.println("A socket create");

            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder requestBuild = new StringBuilder();

            String line = "";
            while (true) {
                line = bufferedReader.readLine();
                if (line == null || line.isEmpty()) {
                    break;
                }
                requestBuild.append(line + "\n");
            }

            String request = requestBuild.toString();
            System.out.println("request: "+ request);
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            String response = handler.apply(request);

            bufferedWriter.write(response);
            bufferedWriter.flush();
            socket.close();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        Step1HttpServer server = new Step1HttpServer(req -> {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "HTTP/1.1 201 ok\n\nGood!\n";
        });
        server.listen(8000);
    }
}

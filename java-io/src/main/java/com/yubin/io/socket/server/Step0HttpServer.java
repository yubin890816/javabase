package com.yubin.io.socket.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 简单的Http服务
 *
 * @author YUBIN
 * @create 2021-01-05
 */
public class Step0HttpServer {

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8000);

        while (true) {
            Socket socket = serverSocket.accept();
            System.out.println("A socket create");

            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder requestBuild = new StringBuilder();

            String line = "";
            while (!(line = bufferedReader.readLine()).isEmpty()) {
                requestBuild.append(line + "\n");
            }

            String request = requestBuild.toString();
            System.out.println("request: "+ request);
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bufferedWriter.write("HTTP/1.1 200 OK\n\nHello World!\n");
            bufferedWriter.flush();
            socket.close();
        }
    }
}

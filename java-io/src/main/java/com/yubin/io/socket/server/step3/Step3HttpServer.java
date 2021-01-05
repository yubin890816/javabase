package com.yubin.io.socket.server.step3;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 简单Http服务之优化线程模型
 *
 * @author YUBIN
 * @create 2021-01-05
 */
public class Step3HttpServer {

    private ServerSocket serverSocket;

    private IHandler httpHandler;

    public Step3HttpServer(IHandler httpHandler) {
        this.httpHandler = httpHandler;
    }

    public void listen(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        while (true) {
            this.accept();
        }
    }

    private void accept() throws IOException {
        Socket socket = serverSocket.accept();
        new Thread(()->{
            try {
                this.handler(socket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void handler(Socket socket) throws IOException {
        Request request = new Request(socket);
        Response response = new Response(socket);
        this.httpHandler.handler(request, response);
    }

    public static void main(String[] args) throws IOException {
        Step3HttpServer server = new Step3HttpServer((req, resp) -> {
            System.out.println(req.getHeaders());
            resp.send("<html><body><h1>Hello World!</h1></body></html>");
        });

        server.listen(8000);
    }
}

package com.yubin.io.socketio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * BIO 多线程的方式
 *
 * @author YUBIN
 * @create 2020-10-31
 */
public class SocketIOProperties {

    //server socket listen property:
    private static final int RECEIVE_BUFFER = 10;
    private static final int SO_TIMEOUT = 0; // 0:表示不超时
    private static final boolean REUSE_ADDR = false;
    private static final int BACK_LOG = 2; // BACK_LOG=2 表示等待线程最多能有2个,超过的都拒绝
    //client socket listen property on server endpoint:
    private static final boolean CLI_KEEPALIVE = false; // 表示是否长连接 false:否 true:是
    private static final boolean CLI_OOB = false; // 是否优先发一个字符试探一下
    private static final int CLI_REC_BUF = 20;
    private static final boolean CLI_REUSE_ADDR = false; // 是否重用地址
    private static final int CLI_SEND_BUF = 20;
    private static final boolean CLI_LINGER = true;
    private static final int CLI_LINGER_N = 0;
    private static final int CLI_TIMEOUT = 0; // 客户端读取数据的超时时间 0:表示不超时
    private static final boolean CLI_NO_DELAY = false; // 不优化 false:不不优化（优化）,指发送量比较小的话会等一会 true:表示有多少条发多少条

    public static void main(String[] args) {
        ServerSocket server = null;
        try {
            server = new ServerSocket();
            server.bind(new InetSocketAddress(9090), BACK_LOG); // BACK_LOG=2 表示等待线程最多能有2个,超过的都拒绝
            server.setReceiveBufferSize(RECEIVE_BUFFER);
            server.setReuseAddress(REUSE_ADDR);
            server.setSoTimeout(SO_TIMEOUT);


            System.out.println("server up use 9090!");
            while (true) {
                try {
                    System.in.read();  //分水岭：

                    Socket client = server.accept();
                    System.out.println("client port: " + client.getPort());

                    client.setKeepAlive(CLI_KEEPALIVE);
                    client.setOOBInline(CLI_OOB);
                    client.setReceiveBufferSize(CLI_REC_BUF);
                    client.setReuseAddress(CLI_REUSE_ADDR);
                    client.setSendBufferSize(CLI_SEND_BUF);
                    client.setSoLinger(CLI_LINGER, CLI_LINGER_N);
                    client.setSoTimeout(CLI_TIMEOUT);
                    client.setTcpNoDelay(CLI_NO_DELAY);

                    new Thread(
                            () -> {
                                while (true) {
                                    try {
                                        InputStream in = client.getInputStream();
                                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                                        char[] data = new char[1024];
                                        int num = reader.read(data);

                                        if (num > 0) {
                                            System.out.println("client read some data is :" + num + " val :" + new String(data, 0, num));
                                        } else if (num == 0) {
                                            System.out.println("client readed nothing!");
                                            continue;
                                        } else {
                                            System.out.println("client readed -1...");
                                            client.close();
                                            break;
                                        }

                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                    ).start();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

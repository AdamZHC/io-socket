package com.hit.adam.io.bio.client;

import com.hit.adam.io.common.AbstractClient;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;

public class BioClient extends AbstractClient {

    public BioClient(int port) {
        super(8201);
    }
    @Override
    public void send() {
        try {
            //创建socket套接字
            Socket socket = new Socket("127.0.0.1", 8201);
            //输出
            OutputStream out = socket.getOutputStream();
            //把字节输出流包装成打印流
            PrintStream printStream = new PrintStream(out);
            printStream.println("hello World! 服务端，你好");
            printStream.flush();
            //自动执行这一句
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

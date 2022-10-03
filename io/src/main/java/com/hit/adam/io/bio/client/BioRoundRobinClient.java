package com.hit.adam.io.bio.client;

import com.hit.adam.io.common.AbstractClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

@SuppressWarnings("All")
public class BioRoundRobinClient extends AbstractClient {

    public BioRoundRobinClient(int port){
        super(port);
    }
    @Override
    public void send() {
        Scanner in = new Scanner(System.in);
        try {
            int idx = 0;
            while(true) {
                //这就是每次建立新连接
                Socket socket = new Socket("127.0.0.1", super.getPort());

                InputStream is = socket.getInputStream();
                OutputStream out = socket.getOutputStream();
                //把字节输出流包装成打印流
                PrintStream printStream = new PrintStream(out);
                printStream.println(idx++ + " socket ===== hello");
                printStream.flush();
                socket.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

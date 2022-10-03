package com.hit.adam.io.bio.server;

import com.hit.adam.io.common.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

@SuppressWarnings("All")
public class BioRoundRobinServer implements Server {
    @Override
    public void receive() {
        try {
            System.out.println("服务端开启");
            ServerSocket serverSocket = new ServerSocket(8201);
            //获取对应的套接字
            System.out.println("==========");
            while(true) {
                //注意这里阻塞获取对应的套接字
                Socket ac = serverSocket.accept();
                System.out.println("==========");
                InputStream is = ac.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String msg = "";
                //获取 ---- 这里就是等待系统调用的输入输出阻塞的过程
                if ((msg = br.readLine()) != null){
                    System.out.println("服务端接收到：" + msg);
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package com.hit.adam.io.bio.server;

import com.hit.adam.io.common.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

@SuppressWarnings("All")
public class BioSimpletonServer implements Server {

    @Override
    public void receive() {
        try {
            //因为不断绑定新的socket所以是不行的
            System.out.println("服务端开启");
            ServerSocket serverSocket = new ServerSocket(8201);
            while(true) {

                //获取对应的套接字
                System.out.println("==========");
                //注意这里阻塞获取对应的套接字
                //网络IO阻塞关键就在，等待套接字的建立连接
                //阻塞、同步：这个函数是阻塞的，没有客户端连接，那就一直卡在这儿等着
                Socket ac = serverSocket.accept();

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

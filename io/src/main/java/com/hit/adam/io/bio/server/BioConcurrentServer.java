package com.hit.adam.io.bio.server;

import com.hit.adam.io.common.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("All")
public class BioConcurrentServer implements Server {
    @Override
    public void receive() {
        ExecutorService es = getThreadPool();
        try {
            ServerSocket socket = new ServerSocket(8201);
            while(true) {
                Socket ac = socket.accept();
                //lambda表达式里面就是需要在进行一层异常处理
                //如果不是用线程池那就是直接来处理对应的问题，每次多开一个线程就是出现问题
                //伪异步IO就是用线程池
                es.execute(()->{
                    InputStream is = null;
                    try {
                        is = ac.getInputStream();
                        BufferedReader br = new BufferedReader(new InputStreamReader(is));
                        String msg = "";
                        //获取 ---- 这里就是等待系统调用的输入输出阻塞的过程
                        if ((msg = br.readLine()) != null){
                            System.out.println("服务端接收到：" + msg + Thread.currentThread().getName());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public ExecutorService getThreadPool() {
        return new ThreadPoolExecutor(
                10,
                200,
                20,
                TimeUnit.SECONDS,
                //如果是无界队列的话，那就可以一直放得下
                new ArrayBlockingQueue<Runnable>(100)
        );
    }
}

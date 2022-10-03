package com.hit.adam.io.bio;

import com.hit.adam.io.bio.client.BioClient;
import com.hit.adam.io.bio.server.BioSimpletonServer;

public class Main {
    public static void main(String[] args) {
        //唯一标记的思路，不允许一个进程里多个线程实现此操作
        Thread t1 = new Thread(() -> {
            new BioSimpletonServer().receive();
        });
        t1.start();
        Thread t2 = new Thread(() -> {
            new BioClient(8201).send();
        });
        t2.start();
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

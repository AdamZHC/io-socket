package com.hit.adam.io.bio.client;


/**
 * 其实并发完全没有必要，直接通过那个循环就行，因为多个发送是等价的，
 */
public class BioClientMain {
    public static void main(String[] args) {
        for(int i = 0; i < 10; ++i)
            new BioClient(8201).send();
    }
}

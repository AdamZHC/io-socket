package com.hit.adam.io.bio.server;

/**
 * 这里很有意思:
 * 对于BIO, 如果允许连接一次之后，可以不断地发送消息也就是，在一次连接中建立管道
 * 但是如果只有有任何一方再次建立连接都会造成传输失败
 * 比如说重复accept(), 重复send(), printStream()
 * 除非重复建立连接，重复accept(), send()
 *
 */
public class BioServerMain {
    public static void main(String[] args) {
        new BioConcurrentServer().receive();
    }
}

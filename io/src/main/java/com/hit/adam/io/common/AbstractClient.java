package com.hit.adam.io.common;

public abstract class AbstractClient implements Client{
    private String ip;
    private int port;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public AbstractClient(String ip) {
        this.ip = ip;
    }

    public AbstractClient(int port) {
        this.port = port;
    }

    public AbstractClient(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

}

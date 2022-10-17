package com.hit.adam.netty.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;

public class HttpServer {
    //对应的端口号
    private final int port;
    //构造方法
    public HttpServer(int port) {
        this.port = port;
    }
    //方法入口
    public static void main(String[] args) throws Exception{
        if(args.length != 1) {
            System.err.println(
                "Usage: " + HttpServer.class.getSimpleName() + "<port>"
            );
            return;
        }
        int port = Integer.parseInt(args[0]);
        new HttpServer(port).start();
    }
    public void start() throws Exception {
        //初始化启动类
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        //事件驱动组
        //TODO
        NioEventLoopGroup eventExecutor = new NioEventLoopGroup();
        //指定事件驱动组
        serverBootstrap.group(eventExecutor)
                //指定Nio的类
                .channel(NioServerSocketChannel.class)
                //指定Channel的处理器
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    /**
                     * 这一套就是来指定对应的处理器的
                     */
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        System.out.println("initChannel : " + socketChannel);
                        //如代码所示，在pipeline后面加许多的处理器
                        socketChannel.pipeline()
                                //解码器
                                .addLast("decoder", new HttpRequestDecoder())
                                //编码器
                                .addLast("encoder", new HttpResponseEncoder())
                                //聚合器——控制读取信息的大小，否则可能会有很多的连接来处理这个问题
                                .addLast("aggregator", new HttpObjectAggregator(1024 * 512))
                                //业务逻辑的处理
                                .addLast( new HttpHandler());
                    }
                })
                //同样是配置的过程
                .option(ChannelOption.SO_BACKLOG, 128);

        ChannelFuture sync = serverBootstrap.bind(port).sync();
        sync.channel().closeFuture().sync();
    }
}

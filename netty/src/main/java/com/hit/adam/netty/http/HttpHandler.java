package com.hit.adam.netty.http;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.AsciiString;

import java.nio.charset.StandardCharsets;

/**
 * 对应的处理器
 */
public class HttpHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private AsciiString contentType = HttpHeaderValues.TEXT_PLAIN;

    @Override
    /**
     * 这里就是所有的处理逻辑，用ChannelHandlerContext FullHttpRequest作为参数
     */
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) throws Exception {
        //打印日志
        System.out.println("class: " + fullHttpRequest.getClass().getName());
        //创建响应对象
        //这里有一个点——在响应消息部分，只需要HttpVersion部分和HttpResponseStatus.OK有对应的指定就可以
        //这样的话，就会被浏览器解析识别，在这里netty进行了一层封装罢了
        DefaultFullHttpResponse defaultFullHttpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                HttpResponseStatus.OK,
                Unpooled.wrappedBuffer("Hello World".getBytes()));
        HttpHeaders httpHeaders = defaultFullHttpResponse.headers();
        //这里就是添加头信息，其实加不加都可以的
        //因为其实有前面的那部分就是已经足够了
        httpHeaders.add(HttpHeaderNames.CONTENT_TYPE, contentType + "; charset=UTF-8");
        httpHeaders.add(HttpHeaderNames.CONTENT_LENGTH, defaultFullHttpResponse.content().readableBytes());
        httpHeaders.add(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        //其实这里算是递交给输出流
        channelHandlerContext.writeAndFlush(defaultFullHttpResponse);
    }

    @Override
    /**
     * 简单来说这里就是channel读结束执行的回调方法
     * 其实主要就是关闭连接
     */
    public void channelReadComplete(ChannelHandlerContext channelHandlerContext) throws Exception{
        System.out.println("channelReadComplete");
        super.channelReadComplete(channelHandlerContext);
        channelHandlerContext.close();
    }

    /**
     * 如果有获取到了异常的话
     * @param channelHandlerContext
     * @param throwable
     * @throws Exception
     */
    public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable throwable) throws Exception {
        System.out.println("exceptionCaught");
        if(throwable != null)
            throwable.printStackTrace();
        if(channelHandlerContext != null)
            channelHandlerContext.close();
    }

}

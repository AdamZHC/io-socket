package com.hit.adam.netty.echo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

//标识这类的实例之间可以在 channel 里面共享

/**
 * 因为这里是ChannelInboundHandlerAdapter所以说其实都是read
 *
 * 方向是固定的，因此就是read()
 */
@ChannelHandler.Sharable
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx,
                            Object msg) {
        ByteBuf in = (ByteBuf) msg;
        //标识这类的实例之间可以在 channel 里面共享
        System.out.println("Server received: " + in.toString(CharsetUtil.UTF_8));
        //将所接收的消息返回给发送者。注意，这还没有冲刷数据
        ctx.write(in);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //将所接收的消息返回给发送者。注意，这还没有冲刷数据
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)
                .addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,
                                Throwable cause) {
        //打印异常堆栈跟踪
        cause.printStackTrace();
        //关闭通道
        ctx.close();
    }
}


package com.hit.adam.netty.live;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.ScheduledFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class LiveHandler extends SimpleChannelInboundHandler<LiveMessage> { // 1
    //即使每次访问都会创建这个类 那也是会使得这个是共享变量
    //当然也有可能这个只创建一次，类似于Servlet，当然这样是有可能的
    private static Map<Integer, LiveChannelCache> channelCache = new HashMap<>();
    private Logger logger = LoggerFactory.getLogger(LiveHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LiveMessage msg) throws Exception {
        /**
         * 这里不会分请求和响应，都是一样的数据也就是LiveMessage
         *
         * 当时这里对应的就是连接，然后进行写入就可在最后的writeAndFlush()
         * 这里也不会创建一个新的消息进行返回
         */
        Channel channel = ctx.channel();
        final int hashCode = channel.hashCode();
        logger.debug("channel hashCode:" + hashCode + " msg:" + msg + " cache:" + channelCache.size());

        if (!channelCache.containsKey(hashCode)) {
            logger.debug("channelCache.containsKey(hashCode), put key:" + hashCode);
            channel.closeFuture().addListener(future -> {
                logger.debug("channel close, remove key:" + hashCode);
                channelCache.remove(hashCode);
            });
            ScheduledFuture scheduledFuture = ctx.executor().schedule(
                    () -> {
                        logger.debug("schedule runs, close channel:" + hashCode);
                        channel.close();
                    }, 10, TimeUnit.SECONDS);
            channelCache.put(hashCode, new LiveChannelCache(channel, scheduledFuture));
        }

        switch (msg.getType()) {
            case LiveMessage.TYPE_HEART: {
                LiveChannelCache cache = channelCache.get(hashCode);
                //这里就是扔进去一个Callable执行的方法就是如果说超过了5s的话，
                //Future其实就是看这个方法有没有执行结束 如果说结束的话，那就会返回不一样的值
                //那就执行对应的方法，或者说另有一个逻辑，但是其实是类似的
                ScheduledFuture scheduledFuture = ctx.executor().schedule(
                        () -> channel.close(), 5, TimeUnit.SECONDS);
                cache.getScheduledFuture().cancel(true);
                cache.setScheduledFuture(scheduledFuture);
                ctx.channel().writeAndFlush(msg);
                break;
            }
            case LiveMessage.TYPE_MESSAGE: {
                channelCache.entrySet().stream().forEach(entry -> {
                    Channel otherChannel = entry.getValue().getChannel();
                    otherChannel.writeAndFlush(msg);
                });
                break;
            }
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        logger.debug("channelReadComplete");
        super.channelReadComplete(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.debug("exceptionCaught");
        if(null != cause) cause.printStackTrace();
        if(null != ctx) ctx.close();
    }
}

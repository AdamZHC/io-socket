package com.hit.adam.netty.live;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 这个泛型可以指定协议的状态
 */
public class LiveDecoder extends ReplayingDecoder<LiveDecoder.LiveState> {

    Logger logger = LoggerFactory.getLogger(LiveDecoder.class);

    public enum LiveState {
        //这里就是表示协议的头部的不同的字段
        //所以说decode方法一定会不断执行
        TYPE, LENGTH, CONTENT
    }

    private LiveMessage message;

    public LiveDecoder() {
        super(LiveState.TYPE);
    }

    @Override
    /**
     * 这里的decode方法一定是会不断执行的，要是一次的话，就不会定义中间的状态了
     */
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        LiveState state = state();
        logger.debug("state:" + state + "messages:" + message);
        //源码验证了我的猜想
        switch (state) {
            case TYPE:
                message = new LiveMessage();
                byte type = byteBuf.readByte();
                //自定义协议
                logger.debug("type: " + type);
                message.setType(type);
                checkpoint(LiveState.LENGTH);
                break;
            case LENGTH:
                int length = byteBuf.readInt();
                message.setLength(length);
                if(length > 0) {
                    checkpoint(LiveState.CONTENT);
                } else {
                    list.add(message);
                    checkpoint(LiveState.TYPE);
                }
                break;
            case CONTENT:
                byte[] bytes = new byte[message.getLength()];
                byteBuf.readBytes(bytes);
                String content = new String(bytes);
                message.setContent(content);
                list.add(message);
                checkpoint(LiveState.TYPE);
                break;
            default:
                throw new IllegalStateException("invalid state:" + state);
        }
    }
}

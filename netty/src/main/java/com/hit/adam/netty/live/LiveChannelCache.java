package com.hit.adam.netty.live;

import io.netty.channel.Channel;

import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;

/**
 * Future可以及时处理返回值并返回所以说这个很点可以获得的很重要的方法
 * 当然这里并不是说获取中间结果或者数据，因为完全可以通过这个操作共享数据实现
 * 这里指的是否返回对应的“结果”，这一点可以通过观察Future的接口来考虑
 * public interface Future<V> {
 *
 *     boolean cancel(boolean mayInterruptIfRunning);
 *
 *     boolean isCancelled();
 *
 *     boolean isDone();
 *
 *     V get() throws InterruptedException, ExecutionException;
 *
 *     V get(long timeout, TimeUnit unit)
 *         throws InterruptedException, ExecutionException, TimeoutException;
 * }
 */
public class LiveChannelCache {
    private Channel channel;
    private ScheduledFuture scheduledFuture;

    public LiveChannelCache(Channel channel, ScheduledFuture scheduledFuture) {
        this.channel = channel;
        this.scheduledFuture = scheduledFuture;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public ScheduledFuture getScheduledFuture() {
        return scheduledFuture;
    }

    public void setScheduledFuture(ScheduledFuture scheduledFuture) {
        this.scheduledFuture = scheduledFuture;
    }
}

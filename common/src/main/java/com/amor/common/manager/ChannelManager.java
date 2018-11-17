package com.amor.common.manager;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * Created by dylan on 2017/10/7.
 */
public class ChannelManager {

    public static final Map<String,Channel> channels = new HashMap<>();

    static {
        Executors.newScheduledThreadPool(1, new ThreadFactory() {
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setDaemon(true);
                return thread;
            }
        }).scheduleWithFixedDelay(()->{
            channels.values()
                    .stream()
                    .forEach(channel -> {
                        if(null != channel && !channel.isActive()){
                            System.out.println("clean the channel ["+channel.id().asLongText()+"] cache......");
                            remove(channel);
                        }
                    });
        },0,1, TimeUnit.MINUTES);
    }
    public static void save(Channel channel){
        String channelId = channel.id().asLongText();
        channels.put(channelId,channel);
    }

    public static Channel get(String channelId){
        return channels.get(channelId);
    }

    public static void remove(Channel channel){
        String channelId = channel.id().asLongText();
        channels.remove(channelId);
    }

    public static void closeOnFlush(Channel channel){
        if (null != channel) {
            remove(channel);
            if(channel.isActive()){
                channel.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
            }
        }
    }
}

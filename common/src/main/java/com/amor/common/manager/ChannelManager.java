package com.amor.common.manager;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dylan on 2017/10/7.
 */
public class ChannelManager {

    public static final Cache<String,Channel> channels = CacheBuilder.newBuilder()
                                                                     .build();

    public static void save(Channel channel){
        String channelId = channel.id().asLongText();
        channels.put(channelId,channel);
    }

    public static Channel get(String channelId){
        return channels.getIfPresent(channelId);
    }

    public static void remove(Channel channel){
        String channelId = channel.id().asLongText();
        channels.invalidate(channelId);
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

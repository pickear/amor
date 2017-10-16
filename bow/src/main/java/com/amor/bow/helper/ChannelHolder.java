package com.amor.bow.helper;

import io.netty.channel.Channel;
import io.netty.channel.ChannelId;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author dylan
 * @time 2017/6/15
 */
public final class ChannelHolder {

    public static Channel clientChannel;
    private final static Map<String,Channel> channels = new ConcurrentHashMap<>();

    public static void holde(Channel channel){
        holde(channel.id().asLongText(),channel);
    }

    public static void holde(String id,Channel channel){
        channels.put(id,channel);
    }

    public static void remove(Channel channel){
        channels.remove(channel.id());
    }

    public static Channel get(String channelId){
        return channels.get(channelId);
    }
    public static Channel getOther(String channelId){
        for(String id : channels.keySet()){
            if(!id.equals(channelId)){
                return channels.get(id);
            }
        }
        return null;
    }

    public static List<Channel> getOthers(String channelId){
        List<Channel> _channels = new ArrayList<>();
        for(String id : channels.keySet()){
            if(!id.equals(channelId)){
                _channels.add(channels.get(id));
            }
        }
        return _channels;
    }

    private ChannelHolder(){}
}

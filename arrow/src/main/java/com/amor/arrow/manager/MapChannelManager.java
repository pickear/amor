package com.amor.arrow.manager;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;
import io.netty.channel.Channel;

import java.util.List;

/**客户端与映射地址Channel关联
 * Created by dylan on 2017/10/13.
 */
public final class MapChannelManager {

    public static Cache<String,Channel> clientChannels = CacheBuilder.newBuilder()
                                                                     .build();

    public static void create(String clientId,Channel channel){
        clientChannels.put(clientId,channel);
    }

    public static Channel get(String clientId){
        return clientChannels.getIfPresent(clientId);
    }

    public static List<Channel> all(){
        return Lists.newArrayList(clientChannels.asMap().values());
    }
    private MapChannelManager(){}
}

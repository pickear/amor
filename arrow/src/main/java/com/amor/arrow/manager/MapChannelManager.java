package com.amor.arrow.manager;

import io.netty.channel.Channel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**客户端与映射地址Channel关联
 * Created by dylan on 2017/10/13.
 */
public final class MapChannelManager {

    public static Map<String,Channel> clientChannels = new HashMap<>();

    public static void create(String clientId,Channel channel){
        clientChannels.put(clientId,channel);
    }

    public static Channel get(String clientId){
        return clientChannels.get(clientId);
    }

    public static List<Channel> all(){
        return new ArrayList<>(clientChannels.values());
    }
    private MapChannelManager(){}
}

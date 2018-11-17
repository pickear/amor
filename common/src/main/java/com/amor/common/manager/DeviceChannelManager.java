package com.amor.common.manager;

import io.netty.channel.Channel;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dylan on 2017/10/3.
 */
public class DeviceChannelManager {

    private static Map<Long,DeviceChannelRelastion> relastions = new HashMap<>();

    public static void create(DeviceChannelRelastion relastion){
        relastions.put(relastion.deviceId,relastion);
    }

    public static DeviceChannelRelastion get(long deviceId){
        return relastions.get(deviceId);
    }

    public static void remove(long deviceId){
        relastions.remove(deviceId);
    }


    public static class DeviceChannelRelastion{

        private long deviceId;
        private Channel clientChannel;
        private Channel bowChannel;

        public DeviceChannelRelastion() {
        }

        public DeviceChannelRelastion(long deviceId, Channel clientChannel, Channel bowChannel) {
            this.deviceId = deviceId;
            this.clientChannel = clientChannel;
            this.bowChannel = bowChannel;
        }

        public long getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(long deviceId) {
            this.deviceId = deviceId;
        }

        public Channel getClientChannel() {
            return clientChannel;
        }

        public void setClientChannel(Channel clientChannel) {
            this.clientChannel = clientChannel;
        }

        public Channel getBowChannel() {
            return bowChannel;
        }

        public void setBowChannel(Channel bowChannel) {
            this.bowChannel = bowChannel;
        }
    }

}

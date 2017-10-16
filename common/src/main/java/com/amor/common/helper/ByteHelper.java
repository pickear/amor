package com.amor.common.helper;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelOutboundBuffer;

import java.nio.charset.Charset;

/**
 * Created by dell on 2017/10/12.
 */
public final class ByteHelper {

    public static byte[] byteBufToByte(ByteBuf byteBuf){
        /*int length = byteBuf.readableBytes();
        System.out.println("byteBuf的长度是:"+length);
        byte[] bytes = new byte[length];
        int index = byteBuf.readerIndex();
        byteBuf.getBytes(index,bytes);
        return bytes;*/
        return ByteBufUtil.getBytes(byteBuf);
    }

    public static String byteBufToString(ByteBuf byteBuf){
        return byteToString(byteBufToByte(byteBuf));
    }
    public static ByteBuf byteToByteBuf(byte[] bytes){

        return Unpooled.wrappedBuffer(bytes);
    }

    public static ByteBuf stringToByteBuf(String byteString){
        return Unpooled.copiedBuffer(byteString,Charset.defaultCharset());
        //return byteToByteBuf(stringToByte(byteString));
    }

    public static String byteToString(byte[] bytes){
        return new String(bytes,Charset.defaultCharset());
    }

    public static byte[] stringToByte(String byteString){
        return byteString.getBytes();
    }

    private ByteHelper(){}
}

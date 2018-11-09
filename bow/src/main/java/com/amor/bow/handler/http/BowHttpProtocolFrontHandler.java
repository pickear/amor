package com.amor.bow.handler.http;

import com.amor.bow.helper.InetAddressHelper;
import com.amor.common.helper.ByteHelper;
import com.amor.common.manager.ChannelManager;
import com.amor.common.manager.DeviceChannelManager;
import com.amor.core.context.ConfigurableContext;
import com.amor.core.model.Device;
import com.amor.core.protocol.HttpProtocol;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.handler.codec.http.*;
import io.netty.util.internal.AppendableCharSequence;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;

/**
 * @author dylan
 * @time 2017/6/15
 */
@Sharable
public class BowHttpProtocolFrontHandler extends ChannelInboundHandlerAdapter{

    private Logger logger = LoggerFactory.getLogger(BowHttpProtocolFrontHandler.class);
    private HeaderParser headerParser = new HeaderParser(new AppendableCharSequence(128),8192);
    private ConfigurableContext context;
    private Channel bowChannel;

    public BowHttpProtocolFrontHandler(ConfigurableContext context) {
        this.context = context;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("客户端[{}]连接!",ctx.channel().id().asLongText());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        final Channel inboundChannel = ctx.channel();
        String channelId = inboundChannel.id().asLongText();

        if(msg instanceof ByteBuf){
            ByteBuf byteBuf = (ByteBuf) msg;
            HttpHeaders headers = headerParser.readHeader(byteBuf);
            String host = headers.get("host");
            boolean isLegal = true;  //url是否合法
            String message = "";  //url非法时回复的消息
            //如果浏览器是通过ip访问的，直接返回。
            if(InetAddressHelper.isIp(host)){
                isLegal = false;
                message = "请通过域名访问";
                logger.info("url非法，通过ip访问无法获得二级域名!");

            }
            //获取二级域名
            String subDomain = StringUtils.split(host,".")[0];
            if(StringUtils.isBlank(subDomain) || StringUtils.equals(subDomain,"www")){
                isLegal = false;
                message = "请通过域名访问";
                logger.info("url非法，通过一级域名访问无法获取二级域名!");
            }

            Device device = context.getBowConfig().getDeviceBySubDomain(subDomain);
            if(null == device){
                isLegal = false;
                message = "通过域名["+host+"]无法找到目标服务器!";
                logger.info("通过二级域名[{}]无法获取域名对应的设备信息!",subDomain);
            }

            if(!isLegal){
                FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK,
                        Unpooled.wrappedBuffer(message.getBytes(Charset.forName("UTF-8"))));
                response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
                response.headers().set(HttpHeaderNames.CONTENT_LENGTH,
                        response.content().readableBytes());
                response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
                ctx.writeAndFlush(response);
                return;
            }

            DeviceChannelManager.DeviceChannelRelastion deviceChannelRelastion = DeviceChannelManager.get(device.getId());
            bowChannel = deviceChannelRelastion.getBowChannel();
            if(bowChannel.isActive()){
                HttpProtocol protocol = new HttpProtocol();
                protocol.setClientId(channelId);
                protocol.setDevice(device);
                protocol.setMsg(ByteHelper.byteBufToByte(byteBuf));
                logger.info("收到http客户端[{}]发送过来的消息，通过bow转发给arrow!",channelId);
                bowChannel.writeAndFlush(protocol).addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture future) throws Exception {
                        if (future.isSuccess()) {
                            ctx.channel().read();
                        } else {
                            logger.warn("通过bow转发http消息给arrow失败，关闭bow与arrow的通道!");
                            future.channel().close();
                        }
                    }
                });
            }
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        logger.info("客户端退出，关闭客户端连接!");
        ChannelManager.closeOnFlush(ctx.channel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        logger.error("与客户端连接异常!",cause);
        ChannelManager.closeOnFlush(ctx.channel());
    }

}

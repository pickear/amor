package com.amor.bow.handler.http;

import com.amor.bow.helper.InetAddressHelper;
import com.amor.common.helper.ByteHelper;
import com.amor.common.manager.ChannelManager;
import com.amor.common.manager.DeviceChannelManager;
import com.amor.core.context.ConfigurableContext;
import com.amor.core.context.ContextHolder;
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
    private ConfigurableContext context = (ConfigurableContext) ContextHolder.getContext();
    private Channel bowChannel;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("receive connection,channel : [{}]",ctx.channel().id().asLongText());
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
                message = "use domain to visit";
                logger.info("can not get the second-level domain through ip");

            }
            //获取二级域名
            String subDomain = StringUtils.split(host,".")[0];
            if(StringUtils.isBlank(subDomain) || StringUtils.equals(subDomain,"www")){
                isLegal = false;
                message = "use domain to visit";
                logger.info("can not get second-level domain through top-level domain");
            }

            Device device = context.getBowConfig().getDeviceBySubDomain(subDomain);
            if(null == device){
                isLegal = false;
                message = "can not get the target server by th domain ["+host+"]";
                logger.info("can not get device message by domain[{}]!",subDomain);
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
                logger.debug("receive message from http client[{}]，replay to arrow!",channelId);
                bowChannel.writeAndFlush(protocol).addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture future) throws Exception {
                        if (future.isSuccess()) {
                            ctx.channel().read();
                        } else {
                            logger.warn("replay http message to arrow fail,close channel!");
                            future.channel().close();
                        }
                    }
                });
            }
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        logger.info("client gone,close client channel");
        ChannelManager.closeOnFlush(ctx.channel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        logger.error("connect client fail",cause);
        ChannelManager.closeOnFlush(ctx.channel());
    }

}

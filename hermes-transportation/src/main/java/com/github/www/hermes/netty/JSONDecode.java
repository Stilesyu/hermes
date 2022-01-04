package com.github.www.hermes.netty;

import com.github.www.hermes.common.utils.jackson.JacksonUtils;
import com.github.www.hermes.protocol.AbstractRequest;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.util.CharsetUtil;

import java.util.List;

/**
 * @author Stiles yu
 * @since 1.0
 */
public class JSONDecode extends MessageToMessageDecoder<ByteBuf> {
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);
        AbstractRequest request = JacksonUtils.deserialize(new String(bytes, CharsetUtil.UTF_8), AbstractRequest.class);
        list.add(request);
    }
}

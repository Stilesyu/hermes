package com.github.www.hermes.netty;

import com.github.www.hermes.common.utils.jackson.JacksonUtils;
import com.github.www.hermes.protocol.AbstractRequest;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.CharsetUtil;

import java.util.List;

/**
 * @author Stiles yu
 * @since 1.0
 */
public class JSONDecode extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        byte[] bytes = new byte[in.readableBytes()];
        in.readBytes(bytes);
        AbstractRequest request = JacksonUtils.deserialize(new String(bytes, CharsetUtil.UTF_8), AbstractRequest.class);
        out.add(request);
    }
}

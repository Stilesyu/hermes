package com.github.www.hermes.netty;

import com.github.www.hermes.common.utils.jackson.JacksonUtils;
import com.github.www.hermes.protocol.AbstractRequest;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

/**
 * @author Stiles yu
 * @since 1.0
 */
public class JSONEncoder extends MessageToMessageEncoder<AbstractRequest> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, AbstractRequest abstractRequest, List<Object> list) throws Exception {
        String json = JacksonUtils.serialize(abstractRequest);
        channelHandlerContext.writeAndFlush(Unpooled.wrappedBuffer(json.getBytes()));
    }
}

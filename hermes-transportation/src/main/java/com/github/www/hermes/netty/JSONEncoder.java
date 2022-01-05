package com.github.www.hermes.netty;

import com.github.www.hermes.common.utils.jackson.JacksonUtils;
import com.github.www.hermes.protocol.AbstractRequest;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author Stiles yu
 * @since 1.0
 */
public class JSONEncoder extends MessageToByteEncoder<AbstractRequest> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, AbstractRequest abstractRequest, ByteBuf byteBuf) {
        byteBuf.writeBytes(JacksonUtils.serialize(abstractRequest).getBytes());
    }
}

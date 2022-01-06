package com.github.www.hermes.netty.handler;

import com.github.www.hermes.protocol.AbstractRequest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author Stiles yu
 * @since 1.0
 */
public class RequestDispatchHandler extends SimpleChannelInboundHandler<AbstractRequest> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, AbstractRequest msg) throws Exception {

    }


}

/*
 *
 *  * Copyright (c) 2011-2021, Stiles Yu(yuxiaochen886@gmail.com),Southern Tree(wutianhuan@qq.com)
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *     http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.github.communication.netty.handler;

import com.github.communication.context.ClientCommunicationContext;
import com.github.communication.context.CommunicationContextHolder;
import com.github.communication.protocol.HeartbeatRequest;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;

import java.io.IOException;

public class HeartBeatHandler extends ChannelDuplexHandler {

    int currentHeartBeatRetries = 0;
    public static String NAME = "heartbeatHandler";


    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            switch (event.state()) {
                case READER_IDLE:
                    if (currentHeartBeatRetries > 3) {
                        ((ClientCommunicationContext) CommunicationContextHolder.getContext()).doConnect();
                    } else {
                        ChannelFuture future = ctx.writeAndFlush(new HeartbeatRequest());
                        if (future.isSuccess()) {
                            currentHeartBeatRetries = 0;
                        } else {
                            currentHeartBeatRetries++;
                        }
                    }
                    break;
                case ALL_IDLE:
                    ctx.channel().close();
                default:
                    break;
            }
        }
        ctx.fireUserEventTriggered(evt);
    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        ((ClientCommunicationContext) CommunicationContextHolder.getContext()).doConnect();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (cause instanceof IOException) {
            return;
        }
        ctx.fireExceptionCaught(cause);
    }
}
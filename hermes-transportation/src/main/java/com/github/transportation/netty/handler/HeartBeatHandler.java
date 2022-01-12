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

package com.github.transportation.netty.handler;

import com.github.transportation.context.ApplicationContext;
import com.github.transportation.context.ApplicationHolder;
import com.github.transportation.protocol.HeartbeatRequest;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;

import static io.netty.handler.timeout.IdleState.READER_IDLE;

public class HeartBeatHandler extends ChannelDuplexHandler {

    int currentHeartBeatRetries = 0;
    public static String NAME = "heartbeat";


    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state().equals(READER_IDLE)) {
                if (currentHeartBeatRetries > 3) {
                    ApplicationHolder.getApplicationContext().doConnect();
                } else {
                    ChannelFuture future = ctx.writeAndFlush(new HeartbeatRequest());
                    if (future.isSuccess()) {
                        currentHeartBeatRetries = 0;
                    } else {
                        currentHeartBeatRetries++;
                    }
                }
            }
        }
        ctx.fireUserEventTriggered(evt);
    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        ApplicationHolder.getApplicationContext().doConnect();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (cause instanceof IOException) {
            return;
        }
        ctx.fireExceptionCaught(cause);
    }
}
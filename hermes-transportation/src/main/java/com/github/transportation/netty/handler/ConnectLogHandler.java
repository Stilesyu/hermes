/*
 * Copyright (c) 2011-2021, Stiles Yu(yuxiaochen886@gmail.com),Southern Tree(wutianhuan@qq.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.transportation.netty.handler;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.SocketAddress;

/**
 * @author Stiles yu
 * @since 1.0
 */
@Slf4j
public class ConnectLogHandler extends ChannelDuplexHandler {

    public boolean isClient;

    public ConnectLogHandler(boolean isClient) {
        this.isClient = isClient;
    }


    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        SocketAddress remoteAddress = ctx.channel().remoteAddress();
        if (remoteAddress == null) {
            return;
        }
        if (isClient) {
            log.info("The channel to communication with the remote server application has been registered.Serve application ip = {}", remoteAddress);
        } else {
            log.info("The channel to communication with the remote client application has been registered.Client application ip = {}", remoteAddress);
        }
    }


    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) {
        SocketAddress remoteAddress = ctx.channel().remoteAddress();
        if (isClient) {
            log.info("The channel to communication with the remote server application has been close.Serve application ip = {}", remoteAddress);
        } else {
            log.info("The channel to communication with the remote client application has been close.Client application ip = {}", remoteAddress);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        if (cause instanceof IOException) {
            SocketAddress remoteAddress = ctx.channel().remoteAddress();
            if (isClient) {
                log.warn("The communication with the server application encounters an IO exception ,and the client application will try to reconnect.Server application ip = {},Exception message = {}", remoteAddress, cause.getMessage());
            } else {
                log.warn("The communication with the client application encounters an IO exception and the channel will be closed.Client application ip = {},Exception message = {}", remoteAddress, cause.getMessage());
                ctx.channel().close();
            }
        }
    }
}

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
 *
 */


package com.github.hermes;

import com.github.hermes.common.constant.CommunicationConstant;
import com.github.hermes.config.ClientNettyConfig;
import com.github.transportation.Application;
import com.github.transportation.netty.handler.RequestEncodeHandler;
import com.github.transportation.protocol.HeartbeatRequest;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Stiles yu
 * @since 1.0
 */
@Slf4j
public class ClientApplication implements Application {


    private final ClientNettyConfig nettyConfig;
    private final EventLoopGroup workerGroup;
    private final Bootstrap bootstrap = new Bootstrap();

    public ClientApplication(ClientNettyConfig nettyConfig) {
        this.nettyConfig = nettyConfig;
        workerGroup = new NioEventLoopGroup();
    }

    @Override
    public void start() {
        bootstrap.group(workerGroup)
                .channel(NioSocketChannel.class)
                .remoteAddress(new InetSocketAddress(nettyConfig.getServerAddress(), nettyConfig.getServerPort()))
                .option(ChannelOption.SO_BACKLOG, 1024)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ch.pipeline().addLast(RequestEncodeHandler.NAME, new RequestEncodeHandler())
                                .addLast(new IdleStateHandler(5, 0, 0))
                                .addLast(HeartBeatHandler.NAME, new HeartBeatHandler(bootstrap))
                        ;
                    }
                });
    }

    @Override
    public void close() {
        workerGroup.shutdownGracefully();
    }


    public void doConnect() {
        bootstrap.connect().addListener((ChannelFutureListener) future -> {
           if (!future.isSuccess()){
               future.channel().eventLoop().schedule()
           }
        });
    }

}

class HeartBeatHandler extends ChannelDuplexHandler {
    int currentHeartBeatRetries;


    public static String NAME = "heartbeat";
    private Bootstrap bootstrap;

    public HeartBeatHandler() {
    }


    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            switch (event.state()) {
                case ALL_IDLE:
                    ctx.channel().close();
                    isClient = false;
                    break;
                case READER_IDLE:
                    doSendHeartBeat(ctx);
            }
        }
        ctx.fireUserEventTriggered(evt);
    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        AtomicBoolean success = new AtomicBoolean(false);
        ScheduledFuture<?> scheduledFuture = Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            Channel channel = bootstrap.bind().channel();
            if (channel.isActive()) {
                success.set(true);
            }
        }, 0, 5, TimeUnit.SECONDS);
        while (true) {
            if (success.get()) {
                boolean cancel = scheduledFuture.cancel(true);
                if (cancel) {
                    break;
                }
            }
        }
    }


    public void doSendHeartBeat(ChannelHandlerContext ctx) {
        ChannelFuture future = ctx.writeAndFlush(new HeartbeatRequest(new Date()));
        future.addListener((ChannelFutureListener) future1 -> {
            if (!future1.isSuccess() && currentHeartBeatRetries > CommunicationConstant.MAX_HEARTBEAT_RETRIES) {

            }
        });
    }

}

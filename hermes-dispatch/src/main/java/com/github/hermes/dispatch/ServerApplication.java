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


package com.github.hermes.dispatch;

import com.github.hermes.common.constant.SystemConstant;
import com.github.hermes.common.utils.SystemUtils;
import com.github.hermes.dispatch.config.ServerNettyConfig;
import com.github.transportation.Application;
import com.github.transportation.netty.handler.ConnectLogHandler;
import com.github.transportation.netty.handler.RequestDecodeHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * @author Stiles yu
 * @since 1.0
 */
@Slf4j
public class ServerApplication implements Application {

    private final EventLoopGroup bossEventLoopGroup;
    private final EventLoopGroup workerEventLoopGroup;
    private final ServerNettyConfig nettyConfig;

    /**
     * Initial event loop group.Linux platform  use {@link EpollEventLoopGroup} by default,while windows  use {@link NioEventLoopGroup} by default
     *
     * @author Stilesyu
     * @since 1.0
     */
    public ServerApplication(ServerNettyConfig config) {
        this.nettyConfig = config;
        if (userEpoll()) {
            bossEventLoopGroup = new EpollEventLoopGroup(1);
            workerEventLoopGroup = new EpollEventLoopGroup(config.getWorkThreadSize());
        } else {
            bossEventLoopGroup = new NioEventLoopGroup();
            workerEventLoopGroup = new NioEventLoopGroup(config.getWorkThreadSize());
        }
    }

    @Override
    public void start() throws InterruptedException {
        ServerBootstrap bootstrap = new ServerBootstrap()
                .group(bossEventLoopGroup, workerEventLoopGroup)
                .channel(userEpoll() ? EpollServerSocketChannel.class : NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .option(ChannelOption.SO_KEEPALIVE, false)
                .localAddress(new InetSocketAddress(nettyConfig.getPort()))
                .handler(new ConnectLogHandler(false))
                .childHandler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) {
                        //TODO use EventExecutorGroup
                        ch.pipeline()
                                .addFirst(new ConnectLogHandler(false))
                                .addLast(RequestDecodeHandler.NAME, new RequestDecodeHandler())
                                .addLast("heartbeat", new IdleStateHandler(0, 0, 200))
                                .addLast(new HeartBeatHandler());
                    }
                });
        ChannelFuture future = bootstrap.bind().sync();
        //waiting until channel closed
        future.channel().closeFuture().sync();
    }


    @Override
    public void close() {
        bossEventLoopGroup.shutdownGracefully();
        workerEventLoopGroup.shutdownGracefully();
    }

    private boolean userEpoll() {
        return SystemUtils.getCurrentSystemOs().equals(SystemConstant.LINUX) && Epoll.isAvailable() && nettyConfig.isUserEpoll();
    }


}
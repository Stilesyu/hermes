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
import com.github.hermes.common.utils.ThreadUtils;
import com.github.hermes.dispatch.config.ServerNettyConfig;
import com.github.transportation.Application;
import com.github.transportation.context.ApplicationContext;
import com.github.transportation.context.ApplicationHolder;
import com.github.transportation.netty.handler.ConnectionLogHandler;
import com.github.transportation.netty.handler.HeartBeatHandler;
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
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Stiles yu
 * @since 1.0
 */
@Slf4j
public class ServerApplication implements Application {

    private final EventLoopGroup bossEventLoopGroup;
    private final EventLoopGroup workerEventLoopGroup;
    private final ServerNettyConfig nettyConfig;
    private final ServerBootstrap bootstrap = new ServerBootstrap();

    /**
     * Initial event loop group.Linux platform  use {@link EpollEventLoopGroup} by default,while windows  use {@link NioEventLoopGroup} by default
     *
     * @author Stilesyu
     * @since 1.0
     */
    public ServerApplication(ServerNettyConfig config) {
        this.nettyConfig = config;
        if (userEpoll()) {
            bossEventLoopGroup = new EpollEventLoopGroup(1, ThreadUtils.createThreadFactory("bossEpollEventLoopGroup"));
            workerEventLoopGroup = new EpollEventLoopGroup(config.getWorkThreadSize(),ThreadUtils.createThreadFactory("workerEpollEventLoopGroup"));
        } else {
            bossEventLoopGroup = new NioEventLoopGroup(1,ThreadUtils.createThreadFactory("bossNioEventLoopGroup"));
            workerEventLoopGroup = new NioEventLoopGroup(config.getWorkThreadSize(),ThreadUtils.createThreadFactory("bossNioEventLoopGroup"));
        }
    }

    @Override
    public void start() throws InterruptedException {
        bootstrap
                .group(bossEventLoopGroup, workerEventLoopGroup)
                .channel(userEpoll() ? EpollServerSocketChannel.class : NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .option(ChannelOption.SO_KEEPALIVE, false)
                .localAddress(new InetSocketAddress(nettyConfig.getPort()))
                .handler(new ConnectionLogHandler())
                .childHandler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) {
                        //TODO use EventExecutorGroup
                        ch.pipeline()
                                .addLast(RequestDecodeHandler.NAME, new RequestDecodeHandler())
                                .addLast("idleStateHandler", new IdleStateHandler(0, 0, 200))
                                .addLast(HeartBeatHandler.NAME, new HeartBeatHandler());
                    }
                });
        ApplicationContext context = ApplicationContext.builder().type(ApplicationContext.Type.SERVER).bootstrap(bootstrap).build();
        ApplicationHolder.bindApplicationContext(context);
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
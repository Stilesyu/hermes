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

import com.github.hermes.common.utils.ThreadUtils;
import com.github.hermes.config.ClientNettyConfig;
import com.github.transportation.Application;
import com.github.transportation.context.AbstractApplicationContext;
import com.github.transportation.context.ApplicationHolder;
import com.github.transportation.context.ClientApplicationContext;
import com.github.transportation.netty.handler.ConnectionLogHandler;
import com.github.transportation.netty.handler.HeartBeatHandler;
import com.github.transportation.netty.handler.RequestEncodeHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

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
        workerGroup = new NioEventLoopGroup(ThreadUtils.createThreadFactory("workerNioEventLoopGroup"));
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
                        ch.pipeline()
                                .addLast(ConnectionLogHandler.NAME, new ConnectionLogHandler())
                                .addLast(RequestEncodeHandler.NAME, new RequestEncodeHandler())
                                .addLast("idleStateHandler", new IdleStateHandler(5, 0, 0))
                                .addLast(HeartBeatHandler.NAME, new HeartBeatHandler())
                        ;
                    }
                });
        AbstractApplicationContext context = ClientApplicationContext.builder().type(AbstractApplicationContext.Type.CLIENT).bootstrap(bootstrap).build();
        ApplicationHolder.bindApplicationContext(context);
        ((ClientApplicationContext) context).doConnect();
    }

    @Override
    public void close() {
        workerGroup.shutdownGracefully();
    }


}



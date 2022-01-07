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


package com.github.www.client.client;

import com.github.www.transportation.Application;
import com.github.www.client.client.config.ClientNettyConfig;
import com.github.www.transportation.netty.handler.JSONEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.net.InetSocketAddress;

/**
 * @author Stiles yu
 * @since 1.0
 */
public class ClientApplication implements Application {


    private final ClientNettyConfig nettyConfig;
    private final EventLoopGroup workerGroup;


    public ClientApplication(ClientNettyConfig nettyConfig) {
        this.nettyConfig = nettyConfig;
        workerGroup = new NioEventLoopGroup();
    }

    @Override
    public void start() throws InterruptedException {
        Bootstrap bootstrap = new Bootstrap()
                .group(workerGroup)
                .channel(NioSocketChannel.class)
                .remoteAddress(new InetSocketAddress(nettyConfig.getServerAddress(), nettyConfig.getPort()))
                .option(ChannelOption.SO_BACKLOG, 1024)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(JSONEncoder.NAME, new JSONEncoder())
                                .addLast(new IdleStateHandler(60, 0, 0));
                    }
                });
        bootstrap.connect().sync();
    }

    @Override
    public void close() {
        workerGroup.shutdownGracefully();
    }
}

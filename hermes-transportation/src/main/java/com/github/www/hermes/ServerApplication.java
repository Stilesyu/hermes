package com.github.www.hermes;

import com.github.www.hermes.common.constant.SystemConstant;
import com.github.www.hermes.common.utils.SystemUtils;
import com.github.www.hermes.config.NettyConfig;
import com.github.www.hermes.netty.handler.JSONDecode;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LoggingHandler;
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
    private final NettyConfig nettyConfig;

    /**
     * Initial event loop group.Linux platform  use {@link EpollEventLoopGroup} by default,while windows  use {@link NioEventLoopGroup} by default
     *
     * @author Stilesyu
     * @since 1.0
     */
    public ServerApplication(NettyConfig config) {
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
    public void open() throws InterruptedException {
        ServerBootstrap bootstrap = new ServerBootstrap()
                .group(bossEventLoopGroup, workerEventLoopGroup)
                .channel(userEpoll() ? EpollServerSocketChannel.class : NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, false)
                .localAddress(new InetSocketAddress(nettyConfig.getPort()))
                .handler(new LoggingHandler())
                .childHandler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) {
                        //TODO use EventExecutorGroup
                        ch.pipeline().addLast(new JSONDecode());
                    }
                });
        bootstrap.bind().sync();
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
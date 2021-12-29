package com.github.www.hermes;

import com.github.www.hermes.netty.JSONEncoder;
import com.github.www.hermes.netty.JsonDecoder;
import com.github.www.hermes.protocol.AbstractRequest;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Stiles yu
 * @since 1.0
 */
@Slf4j
public class ServerApplication implements Application {


    @Override
    public void open() throws InterruptedException {
        EventLoopGroup masterGroup = new NioEventLoopGroup();
        EventLoopGroup slaveGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(masterGroup, slaveGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            // 这里将LengthFieldBasedFrameDecoder添加到pipeline的首位，因为其需要对接收到的数据
                            // 进行长度字段解码，这里也会对数据进行粘包和拆包处理
                            ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(1024, 0, 2, 0, 2));
                            ch.pipeline().addLast(new LengthFieldPrepender(2));
                            ch.pipeline().addLast(new JsonDecoder());
                            ch.pipeline().addLast(new JSONEncoder());
                            ch.pipeline().addLast(new EchoServerHandler());
                        }
                    });
            ChannelFuture future = bootstrap.bind(9607).sync();
            future.channel().closeFuture().sync();
        } finally {
            masterGroup.shutdownGracefully();
            slaveGroup.shutdownGracefully();
        }
    }


    static class EchoServerHandler extends SimpleChannelInboundHandler<AbstractRequest> {

        protected void channelRead0(ChannelHandlerContext ctx, AbstractRequest msg) throws Exception {
            System.out.println("receive from client: " + msg);
        }


    }

    @Override
    public void close() {

    }
}
package com.github.www.hermes;

import com.github.www.hermes.common.ApiKeys;
import com.github.www.hermes.netty.JSONEncoder;
import com.github.www.hermes.netty.JsonDecoder;
import com.github.www.hermes.protocol.AbstractRequest;
import com.github.www.hermes.protocol.HeartbeatRequest;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;


/**
 * @author Stiles yu
 * @since 1.0
 */
public class ClientApplication implements Application {

    @Override
    public void open() throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                                 @Override
                                 protected void initChannel(SocketChannel ch) throws Exception {
                                     ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(1024, 0, 2, 0, 2));
                                     ch.pipeline().addLast(new LengthFieldPrepender(2));
                                     ch.pipeline().addLast(new JsonDecoder());
                                     ch.pipeline().addLast(new JSONEncoder());
                                     ch.pipeline().addLast(new EchoClientHandler());
                                 }
                             }
                    );
            ChannelFuture future = bootstrap.connect("localhost",9607).sync();
            future.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }

    @Override
    public void close() {

    }

    static class EchoClientHandler extends SimpleChannelInboundHandler<AbstractRequest> {
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, AbstractRequest msg) throws Exception {
            System.out.println("receive message from server: " + msg);
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            AbstractRequest request = new HeartbeatRequest();
            request.setCode(ApiKeys.HEARTBEAT);
            request.setVersion((short) 2);
            ctx.write(request);
            System.out.println("发送成功");
        }
    }


}

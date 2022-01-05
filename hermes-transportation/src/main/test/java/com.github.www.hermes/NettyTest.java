package com.github.www.hermes;

import com.github.www.hermes.common.utils.jackson.JacksonUtils;
import com.github.www.hermes.config.NettyConfig;
import com.github.www.hermes.netty.JSONDecode;
import com.github.www.hermes.netty.JSONEncoder;
import com.github.www.hermes.protocol.HeartbeatRequest;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.jupiter.api.Test;

import java.util.Date;

/**
 * @author Stiles yu
 * @since 1.0
 */
public class NettyTest {


    @Test
    public void client() throws InterruptedException {
        ClientApplication clientApplication = new ClientApplication();
        clientApplication.open();
    }

    @Test
    public void server() throws InterruptedException {
        NettyConfig config = new NettyConfig();
        ServerApplication serverApplication = new ServerApplication(config);
        serverApplication.open();
    }

    @Test
    public void JSONEncode() {
        HeartbeatRequest request = new HeartbeatRequest(new Date());
        EmbeddedChannel embeddedChannel = new EmbeddedChannel(new JSONEncoder());
        embeddedChannel.writeOutbound(request);
        embeddedChannel.finish();
        ByteBuf buffer = embeddedChannel.readOutbound();
        printByteBuf(buffer);
    }

    @Test
    public void JSONDecode() {
        HeartbeatRequest request = new HeartbeatRequest(new Date());
        String json = JacksonUtils.serialize(request);
        EmbeddedChannel embeddedChannel = new EmbeddedChannel(new JSONDecode());
        embeddedChannel.writeInbound(Unpooled.buffer().writeBytes(json.getBytes()));
        HeartbeatRequest heartbeatRequest = embeddedChannel.readInbound();
        System.out.println(JacksonUtils.serialize(heartbeatRequest));;

    }


    private void printByteBuf(ByteBuf buffer) {
        for (int i = 0; i < buffer.capacity(); i++) {
            byte b = buffer.getByte(i);
            System.out.print((char) b);
        }
    }

}

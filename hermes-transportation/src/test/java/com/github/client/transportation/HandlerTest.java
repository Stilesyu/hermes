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


package com.github.client.transportation;

import com.github.hermes.common.utils.jackson.JacksonUtils;
import com.github.transportation.netty.handler.ConnectLogHandler;
import com.github.transportation.netty.handler.RequestDecodeHandler;
import com.github.transportation.netty.handler.RequestEncodeHandler;
import com.github.transportation.protocol.HeartbeatRequest;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.jupiter.api.Test;

import java.util.Date;

/**
 * @author Stiles yu
 * @since 1.0
 */
public class  HandlerTest{



    @Test
    public void JSONEncode() {
        HeartbeatRequest r1 = new HeartbeatRequest(new Date());
        HeartbeatRequest r2 = new HeartbeatRequest(new Date());
        EmbeddedChannel embeddedChannel = new EmbeddedChannel(new RequestEncodeHandler());
        embeddedChannel.writeOutbound(r1, r2);
        embeddedChannel.finish();
        ByteBuf buffer = embeddedChannel.readOutbound();
        printByteBuf(buffer);
    }

    @Test
    public void RequestDecodeHandler() {
        EmbeddedChannel embeddedChannel = new EmbeddedChannel(new RequestDecodeHandler());
        HeartbeatRequest request = new HeartbeatRequest(new Date());
        String json = JacksonUtils.serialize(request);
        ByteBuf byteBuf = Unpooled.buffer().writeBytes(json.getBytes());
        HeartbeatRequest r2 = new HeartbeatRequest(new Date());
        String jj = JacksonUtils.serialize(request);
        ByteBuf by = Unpooled.buffer().writeBytes(jj.getBytes());
        embeddedChannel.writeInbound(byteBuf, by);
        HeartbeatRequest heartbeatRequest = embeddedChannel.readInbound();
        System.out.println(JacksonUtils.serialize(heartbeatRequest));
    }



    private void printByteBuf(ByteBuf buffer) {
        for (int i = 0; i < buffer.capacity(); i++) {
            byte b = buffer.getByte(i);
            System.out.print((char) b);
        }
    }

}

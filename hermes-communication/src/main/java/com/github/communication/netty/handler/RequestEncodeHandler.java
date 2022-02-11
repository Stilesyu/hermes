/*
 *
 *  * Copyright (c) 2011-2021, Stiles Yu(yuxiaochen886@gmail.com)
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



package com.github.communication.netty.handler;

import com.github.hermes.common.utils.jackson.JacksonUtils;
import com.github.communication.protocol.AbstractRequest;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author Stiles yu
 * @since 1.0
 */
public class RequestEncodeHandler extends MessageToByteEncoder<AbstractRequest> {

    public static String NAME = "convert abstractRequest to bytes";

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, AbstractRequest abstractRequest, ByteBuf byteBuf) {
        byteBuf.writeBytes(JacksonUtils.serialize(abstractRequest).getBytes());
    }
}

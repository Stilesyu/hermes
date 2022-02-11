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
 */

package com.github.communication.context;

import com.github.communication.exception.HermesConnectException;
import com.github.hermes.common.exception.HermesParameterException;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import lombok.experimental.SuperBuilder;

import java.util.concurrent.TimeUnit;

/**
 * @author Stiles yu
 * @since 1.0
 */
@SuperBuilder
public class ClientCommunicationContext extends AbstractCommunicationContext {

    private Channel channel;


    public Channel getChanel() {
        return channel;
    }


    public void doConnect() {
        if (super.bootstrap == null) {
            throw new HermesParameterException("Bootstrap not initialized");
        }
        if (super.bootstrap instanceof Bootstrap) {
            ((Bootstrap) super.bootstrap).connect().addListener((ChannelFutureListener) future -> {
                if (!future.isSuccess()) {
                    future.channel().eventLoop().schedule(this::doConnect, 10, TimeUnit.SECONDS);
                    throw new HermesConnectException();
                } else {
                    this.channel = future.channel();
                }
            });
        } else {
            throw new HermesParameterException("Only Client Application can call the doConnect method");
        }
    }


}

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
 */

package com.github.transportation.context;

import com.github.hermes.common.exception.HermesParameterException;
import com.github.transportation.exception.HermesConnectException;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFutureListener;
import lombok.Builder;

import java.util.concurrent.TimeUnit;

/**
 * @author Stiles yu
 * @since 1.0
 */
@Builder
public class ApplicationContext {

    public Type type;
    public Bootstrap bootstrap;


    public void doConnect() {
        if (bootstrap == null) {
            throw new HermesParameterException("Bootstrap not initialized");
        }
        bootstrap.connect().addListener((ChannelFutureListener) future -> {
            if (!future.isSuccess()) {
                future.channel().eventLoop().schedule(this::doConnect, 10, TimeUnit.SECONDS);
            }
            throw new HermesConnectException();
        });
    }

    public enum Type {
        CLIENT,
        SERVER
    }
}

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

package com.github.communication;

import com.github.communication.context.ClientCommunicationContext;
import com.github.communication.context.CommunicationContextHolder;
import com.github.communication.protocol.AbstractRequest;
import com.github.communication.protocol.AbstractResponse;
import com.github.communication.utils.SyncFuture;
import com.github.communication.exception.HermesRequestException;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Stiles yu
 * @since 1.0
 */
@Slf4j
public class ClientRemoteProcedureCall implements RemoteProcedureCall {


    private final ClientCommunicationContext context = ((ClientCommunicationContext) CommunicationContextHolder.getContext());
    private final Channel channel = context.getChanel();

    @Override
    public void invokeOnWay(String remoteAddress, AbstractRequest request) {
        //TODO limit max number of request through Semaphore
        try {
            channel.writeAndFlush(request).addListener((ChannelFutureListener) future -> {
                if (!future.isSuccess()) {
                    log.warn("invokeOnWay:failed to send request.Remote address:{}", channel.remoteAddress());
                }
            });
        } catch (Exception e) {
            log.warn("invokeOnWay:failed to send request.Remote address:{}", channel.remoteAddress());
            throw new HermesRequestException(channel.remoteAddress());
        }
    }

    @Override
    public AbstractResponse invokeSync(String remoteAddress, AbstractRequest request, long timeoutTime) {
        try {
            channel.writeAndFlush(request).addListener((ChannelFutureListener) future -> {
                if (future.isSuccess()) {
                    context.putResponseMapping(request.getRequestId(), new SyncFuture(timeoutTime));
                    return;
                }
                log.warn("invokeOnWay:failed to send request.Remote address:{}", channel.remoteAddress());
                throw new HermesRequestException(channel.remoteAddress());
            });
            return context.getSyncFuture(request.getRequestId()).getResponse();
        } catch (Exception e) {
            log.warn("invokeOnWay:failed to send request.Remote address:{},error message:{}", channel.remoteAddress(), e.getMessage());
            throw new HermesRequestException(channel.remoteAddress());
        }
    }

    @Override
    public AbstractRequest invokeAsync(String remoteAddress, AbstractRequest request, long timeoutTime) {
        return null;
    }


}

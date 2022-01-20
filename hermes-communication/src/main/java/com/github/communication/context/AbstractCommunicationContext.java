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

package com.github.communication.context;

import com.github.communication.utils.SyncFuture;
import io.netty.bootstrap.AbstractBootstrap;
import lombok.experimental.SuperBuilder;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author Stiles yu
 * @since 1.0
 */

@SuperBuilder
public abstract class AbstractCommunicationContext {

    protected Type type;
    protected AbstractBootstrap<?, ?> bootstrap;


    public Type getType() {
        return type;
    }

    public AbstractBootstrap<?, ?> getBootstrap() {
        return bootstrap;
    }

    /**
     * key:requestId
     */
    private final ConcurrentMap<Long, SyncFuture> responseMapping = new ConcurrentHashMap<>();

    public void putResponseMapping(Long requestId, SyncFuture syncFuture) {
        responseMapping.put(requestId, syncFuture);
    }

    public void removeResponseMapping(Long requestId) {
        responseMapping.remove(requestId);
    }

    public SyncFuture getSyncFuture(Long requestId) {
        return responseMapping.get(requestId);
    }


    public enum Type {
        CLIENT,
        SERVER
    }
}

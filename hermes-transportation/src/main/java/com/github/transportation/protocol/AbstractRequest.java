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

package com.github.transportation.protocol;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.github.hermes.common.constant.ApiConstant;
import com.github.hermes.common.ApiKeys;
import lombok.Data;

/**
 * @author Stiles yu
 * @since 1.0
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "code", visible = true, include = JsonTypeInfo.As.EXISTING_PROPERTY)
@JsonSubTypes({
        @JsonSubTypes.Type(value = HeartbeatRequest.class, name = ApiConstant.HEARTBEAT)
})
@Data
public abstract class AbstractRequest {
    private Long requestId;
    private short code;
    private short version;

    public void code(ApiKeys keys) {
        this.code = keys.getCode();
    }

    public void version(ApiKeys keys) {
        this.version = keys.getHighestSupportVersion();
    }
}

/*
 * Copyright (c) 2011-2021, Stiles Yu(yuxiaochen886@gmail.com),Southern Tree(wutianhuan@qq.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.github.hermes.dispatch.config;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author Stiles yu
 * @since 1.0
 */
@Data
@Accessors(chain = true)
public class ServerNettyConfig {

    private boolean userEpoll = false;
    private int workThreadSize = 5;
    private int serverPort = 9607;
    private int port = 9607;


}

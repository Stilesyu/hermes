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

package com.github.transportation.exception;

import com.github.hermes.common.constant.ExceptionInfoConstant;
import com.github.hermes.common.exception.HermesException;

/**
 * @author Stiles yu
 * @since 1.0
 */
public class HermesConnectException extends HermesException {

    public HermesConnectException() {
        super(ExceptionInfoConstant.UNABLE_TO_CONNECT_SERVER);

    }

    public HermesConnectException(String message) {
        super(message);
    }
}

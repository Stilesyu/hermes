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

package com.github.transportation.utils;

import com.github.transportation.protocol.AbstractResponse;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author Stiles yu
 * @since 1.0
 */
public class SyncFuture {

    private final CountDownLatch countDownLatch = new CountDownLatch(1);
    private AbstractResponse response;
    private final long timeoutTime;


    public SyncFuture(long timeoutTime) {
        this.timeoutTime = timeoutTime;
    }

    public AbstractResponse getResponse() throws InterruptedException {
        try {
            if (countDownLatch.getCount() > 0) {
                boolean isZero = countDownLatch.await(timeoutTime, TimeUnit.SECONDS);
                if (isZero) {
                    return response;
                }
            }
        } finally {
            countDownLatch.countDown();
        }
        return null;
    }

    public void receive(AbstractResponse response) {
        this.response = response;
        countDownLatch.countDown();
    }


}

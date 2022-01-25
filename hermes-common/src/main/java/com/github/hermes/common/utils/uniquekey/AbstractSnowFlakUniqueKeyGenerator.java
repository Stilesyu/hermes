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

package com.github.hermes.common.utils.uniquekey;

import com.lmax.disruptor.RingBuffer;

/**
 * Designed according to the twitter snowflake algorithm({https://github.com/twitter-archive/snowflake})
 * <p>
 * 0                000000000000000000          00                   00000                00000
 * unused(1bit)     timestamp(42bit)            businessId(5bit)     workerId(5bit)       sequence(10bit)
 *
 * @author Stiles yu
 * @since 1.0
 */
public abstract class AbstractSnowFlakUniqueKeyGenerator implements UniqueKeyGenerator {

    private final int workerBits = 0;
    private final int sequenceBits = 0;
    private final int businessId = 2;
    //maxWorkerBits = 2^5-1
    private long maxWorkerBits = ~(-1 << workerBits);
    //
    private RingBuffer<Long> buffer;

    @Override
    public long generate() {
        return buffer.next();
    }


    private void filling() {

    }

    private boolean needToBeFilled() {
        return false;
    }


    protected abstract void batchGet();


}

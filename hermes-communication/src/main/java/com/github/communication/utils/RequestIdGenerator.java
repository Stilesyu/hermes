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

package com.github.communication.utils;

import com.github.hermes.common.exception.HermesException;
import com.github.hermes.common.utils.uniquekey.AbstractSnowFlakeUniqueKeyGenerator;

/**
 * unused(1bit)  | timestamp(42bit)  |  workerId(5bit) |  flag(2bit)  | sequence(14bit)
 *
 * @author Stiles yu
 * @since 1.0
 */
public class RequestIdGenerator extends AbstractSnowFlakeUniqueKeyGenerator {
    private final static int workerIdBits = 5;
    private final static int sequenceBits = 14;
    private final static int flagBits = 2;
    //maxWorkerBits = 2^5-1 = 31
    private final static long maxWorkerId = ~(-1 << workerIdBits);
    //maxSequenceBits = 16383
    private final static int maxSequence = ~(-1 << sequenceBits);
    private final static int maxFlag = ~(-1 << flagBits);
    private final static int flagShift = sequenceBits;
    private final static int workerIdShift = flagShift + flagBits;
    private final static int timestampShift = workerIdShift + workerIdBits;
    private final long workerId;
    private long lastTimestamp = -1;
    private int flag;
    private int lastSequence = 0;
    private final static long genesis = 1643538390964L;

    public RequestIdGenerator(long workerId) {
        //Must add one because bufferSize must be a power of 2
        super(maxSequence + 1);
        this.workerId = workerId;
    }


    @Override
    protected Long[] nextIds(int size) {
        long timestamp = System.currentTimeMillis();
        if (this.lastTimestamp > timestamp) {
            if (++flag > maxFlag) {
                throw new HermesException(String.format("Clock moved backwards more than 4 times.  Refusing to generate id for %d milliseconds",
                        lastTimestamp - timestamp));
            }
            flag++;
        } else if (lastTimestamp < timestamp) {
            flag = 0;
            lastSequence = 0;
            lastTimestamp = timestamp;
        }
        int theRemainingSize = maxSequence * 4 - flag * maxSequence + lastSequence;
        if (theRemainingSize < size) {
            throw new HermesException("RequestIdGenerator generates up to 16383*4 ids per millisecond");
        }
        Long[] result = new Long[size];
        long fixedBit = fixedBitGen();
        for (int j = 0; j < size; j++) {
            result[j] = fixedBit | lastSequence++;
            if (lastSequence > maxSequence) {
                lastSequence = 0;
                flag++;
                fixedBit = fixedBitGen();
            }
        }
        return result;
    }


    private long fixedBitGen() {
        return (lastTimestamp - genesis) << timestampShift |
                workerId << workerIdShift | (long) flag << flagShift;
    }


    @Override
    protected int fillingSize() {
        //return 511
        return maxSequence >> 5;
    }

}

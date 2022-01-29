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

package com.github.hermes.common.datastrcut;

import com.github.hermes.common.exception.HermesException;
import com.github.hermes.common.exception.HermesParameterException;

/**
 * Solve false sharing with reference to the Disruptor
 * (https://github.com/LMAX-Exchange/disruptor)
 *
 * @author Stiles yu
 * @since 1.0
 */
abstract class RingBufferPad {
    private long p1, p2, p3, p4, p5, p6, p7;
}

public class RingBuffer<E> extends RingBufferPad {
    private final Object[] entries;
    private int header = 1;
    private int tail = 0;
    private final int bufferSize;
    private long p11, p12, p13, p14, p15, p16, p17;

    public RingBuffer(int bufferSize) {
        if (bufferSize < 1) {
            throw new HermesParameterException("bufferSize must not be less than 1");
        }
        if (Integer.bitCount(bufferSize) != 1) {
            throw new HermesParameterException("bufferSize must be a power of 2");
        }
        this.bufferSize = bufferSize;
        entries = new Object[bufferSize + bufferSize/*mirror*/];
    }


    /**
     * TO make object memory as contiguous as possible
     *
     * @author Stilesyu
     * @since 1.0
     */
    private void initEntries() {
        for (int i = 0; i < entries.length; i++) {
            entries[i] = new Object();
        }
    }

    public boolean isFull() {
        return header == (tail ^ bufferSize);
    }


    public boolean isEmpty() {
        return header == tail;
    }


    public void saveBatch(E[] entries) {
        int size = entries.length;
        // ((tail & (size - 1)) ^ bufferSize) = (tail%size)^bufferSize
        if (isFull() || size > bufferSize || tail == ((header & (size - 1)) ^ bufferSize)) {
            throw new HermesException("RingBuffer has no more space to hold data");
        }
        for (E entry : entries) {
            if (header + 1 > bufferSize * 2 - 1) {
                this.entries[header = 0] = entry;
            } else {
                this.entries[++header] = entry;
            }
        }
    }

    public Object[] readBatch(int size) {
        if (isEmpty() || size > bufferSize || header == (tail & (size - 1))) {
            throw new HermesException("RingBuffer has no more data to read");
        }
        Object[] result = new Object[size];
        for (int i = 0; i < size; i++) {
            if (tail+1>bufferSize*2-1){
                result[i] = entries[tail=0];
            }else {
                result[i] = entries[++tail];
            }
        }
        return result;
    }

}
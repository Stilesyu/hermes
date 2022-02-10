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
 * Solve false sharing(https://mechanical-sympathy.blogspot.com/2011/07/false-sharing.html)
 *
 * @author Stiles yu
 * @since 1.0
 */
abstract class RingBufferPad {
    private long p1, p2, p3, p4, p5, p6, p7;
}

public class RingBuffer<E> extends RingBufferPad {
    private final Object[] entries;
    private int header = 0;
    private int tail = 0;
    private final int bufferSize;
    private final int size;
    private long p11, p12, p13, p14, p15, p16, p17;

    public RingBuffer(int bufferSize) {
        if (bufferSize < 1) {
            throw new HermesParameterException("bufferSize must not be less than 1");
        }
        if (Integer.bitCount(bufferSize) != 1) {
            throw new HermesParameterException("bufferSize must be a power of 2");
        }
        this.bufferSize = bufferSize;
        this.size = bufferSize * 2;
        entries = new Object[bufferSize + bufferSize/*mirror*/];
    }


    /**
     * To make object memory as contiguous as possible
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
        int saveSize = entries.length;
        //(header + saveSize) & (size - 1) = (header+saveSize) % size
        if (isFull() || saveSize > bufferSize || (tail ^ bufferSize) < ((header + saveSize) & (size - 1))) {
            throw new HermesException("RingBuffer has no more space to hold data");
        }
        for (E entry : entries) {
            if (header + 1 > size - 1) {
                this.entries[header = 0] = entry;
            } else {
                this.entries[++header] = entry;
            }
        }
    }

    @SuppressWarnings("unchecked")
    public E read() {
        if (isEmpty()) {
            throw new HermesException("RingBuffer has no more data to read");
        }
        if (tail + 1 > size - 1) {
            return (E) entries[tail = 0];
        } else {
            return (E) entries[++tail];
        }
    }


    public int readableSize() {
        if (header >= tail) {
            return header - tail;
        } else {
            return header - tail + size;
        }
    }

    public int writeableSize() {
        if (header >= tail) {
            return tail + bufferSize - header;
        } else {
            return tail - bufferSize - header;
        }
    }


}
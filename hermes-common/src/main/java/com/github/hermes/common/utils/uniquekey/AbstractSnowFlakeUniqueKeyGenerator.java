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

import com.github.hermes.common.datastrcut.RingBuffer;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public abstract class AbstractSnowFlakeUniqueKeyGenerator implements UniqueKeyGenerator {


    private final RingBuffer<Long> buffer;

    /**
     * @param bufferSize:Maximum size of RingBuffer,must be a power of 2
     * @author Stilesyu
     * @since 1.0
     */
    public AbstractSnowFlakeUniqueKeyGenerator(int bufferSize) {
        this.buffer = new RingBuffer<>(bufferSize);
        buffer.saveBatch(nextIds(bufferSize));
    }


    @Override
    public synchronized long generate() {
        int fillingSize = fillingThreshold();
        if (this.buffer.readableSize() <= fillingSize) {
            int needFilledSize = buffer.writeableSize();
            buffer.saveBatch(nextIds(needFilledSize));
        }
        return buffer.read();
    }

    protected abstract Long[] nextIds(int size);


    protected abstract int fillingThreshold();


}

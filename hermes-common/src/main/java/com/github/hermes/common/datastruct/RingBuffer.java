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

package com.github.hermes.common.datastruct;

/**
 * @author Stiles yu
 * @since 1.0
 */
public class RingBuffer<E> {

    /**
     * Default max length = 1024
     * The last position is used to determine whether it is full
     */
    private int maxLength = 1024 + 1;
    Object[] buffer = new Object[maxLength];
    private int tail = 0;
    private int head = 0;


    public RingBuffer(int maxLength) {
        this.maxLength = maxLength;
    }




    public void add(E e) {

    }

    public boolean isFull() {
        if ()
    }


    public Object get() {
    }


}

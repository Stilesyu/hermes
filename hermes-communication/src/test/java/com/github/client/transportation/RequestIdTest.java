/*
 * Copyright (c) 2011-2021, Stiles Yu(yuxiaochen886@gmail.com)
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

package com.github.client.transportation;

import com.github.communication.utils.RequestIdGenerator;
import com.sun.tools.javac.util.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

/**
 * @author Stiles yu
 * @since 1.0
 */
public class RequestIdTest {


    @Test
    public void checkRepeat() {
        RequestIdGenerator generator = new RequestIdGenerator(1);
        int size = 16383 * 100;
        Long[] result = new Long[size];
        for (int i = 0; i < size; i++) {
            result[i] = generator.generate();
        }
        Assertions.assertEquals(size, (int) Arrays.stream(result).distinct().count());
    }

    @Test
    public void perform() {
        RequestIdGenerator generator = new RequestIdGenerator(1);
        int size = 16383 * 3;
        Long[] result = new Long[size];
        long start = System.currentTimeMillis();
        for (int i = 0; i < size; i++) {
            result[i] = generator.generate();
        }
        System.out.println(System.currentTimeMillis() - start);
    }

}

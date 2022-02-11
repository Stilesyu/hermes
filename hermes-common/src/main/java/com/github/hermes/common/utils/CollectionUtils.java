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

package com.github.hermes.common.utils;

import java.util.List;

/**
 * @author Stiles yu
 * @since 1.0
 */
public class CollectionUtils {

    public static boolean isEmpty(List<Object> source) {
        return source == null || source.size() == 0;
    }


    public static boolean isNotEmpty(List<Object> source) {
        return !isEmpty(source);
    }


}

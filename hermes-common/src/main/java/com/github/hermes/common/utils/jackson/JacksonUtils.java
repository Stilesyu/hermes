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

package com.github.hermes.common.utils.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JacksonUtils {

    public static ObjectMapper objectMapper = new ObjectMapper();

    public static <T> String serialize(T t) throws JacksonSerializationException {
        return serialize(t, true);
    }

    public static <T> String serialize(T t, boolean throwEx) throws JacksonSerializationException {
        try {
            return objectMapper.writeValueAsString(t);
        } catch (JsonProcessingException var3) {
            if (throwEx) {
                throw new JacksonSerializationException(var3);
            } else {
                return null;
            }
        }
    }

    public static <T> String serialize(T t, Class<?> view) {
        return serialize(t, view, true);
    }

    public static <T> String serialize(T t, Class<?> view, boolean throwEx) throws JacksonSerializationException {
        try {
            return objectMapper.writerWithView(view).writeValueAsString(t);
        } catch (JsonProcessingException var4) {
            if (throwEx) {
                throw new JacksonSerializationException(var4);
            } else {
                return null;
            }
        }
    }

    public static <T> T deserialize(String s, Class<T> type) throws JacksonSerializationException {
        return deserialize(s, type, true);
    }

    public static <T> T deserialize(String s, Class<T> type, boolean throwEx) throws JacksonSerializationException {
        try {
            return objectMapper.readValue(s, type);
        } catch (IOException var4) {
            if (throwEx) {
                throw new JacksonSerializationException(var4);
            } else {
                return null;
            }
        }
    }

    public static <T> T deserialize(String s, Class<T> type, Class<?> view) throws JacksonSerializationException {
        return deserialize(s, type, view, true);
    }

    public static <T> T deserialize(String s, Class<T> type, Class<?> view, boolean throwEx) throws JacksonSerializationException {
        try {
            return objectMapper.readerWithView(view).readValue(s, type);
        } catch (IOException var5) {
            if (throwEx) {
                throw new JacksonSerializationException(var5);
            } else {
                return null;
            }
        }
    }

    public static <T> T deserialize(String s, TypeReference<T> typeReference) throws JacksonSerializationException {
        return deserialize(s, typeReference, true);
    }

    public static <T> T deserialize(String s, TypeReference<T> typeReference, boolean throwEx) throws JacksonSerializationException {
        try {
            return objectMapper.readValue(s, typeReference);
        } catch (IOException var4) {
            if (throwEx) {
                throw new JacksonSerializationException(var4);
            } else {
                return null;
            }
        }
    }
}

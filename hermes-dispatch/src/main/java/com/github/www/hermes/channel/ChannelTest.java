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

package com.github.www.hermes.channel;

import com.github.www.hermes.channel.User.UserItem;

import java.util.ArrayList;

import com.github.www.hermes.common.jackson.JacksonUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
public class ChannelTest {

    public static void read(FileChannel fileChannel) throws IOException, InterruptedException {
        ByteBuffer buf = ByteBuffer.allocate(48);
        int bytesRead = fileChannel.read(buf);
        while (bytesRead != -1) {
            System.out.println("Read " + bytesRead);
            buf.flip();
            while (buf.hasRemaining()) {
                System.out.print((char) buf.get());
            }
            buf.clear();
            bytesRead = fileChannel.read(buf);
        }
        System.out.println("wan");
    }

    public static void write(FileChannel channel, User user) throws IOException {
        byte[] bytes = JacksonUtils.serialize(user).getBytes(StandardCharsets.UTF_8);
        ByteBuffer buf = ByteBuffer.allocate(bytes.length);
        buf.clear();
        buf.put(bytes);
        buf.flip();
        long start = System.currentTimeMillis();
        log.info("开始刷盘");
        while (buf.hasRemaining()) {
            channel.write(buf);
        }
        long time = System.currentTimeMillis() - start;
        log.info("刷盘结束：{}", time);

    }

    public static void readAndWrite(FileChannel channel) throws IOException {
//        write(channel.position(channel.size()));
    }

    public static void force(FileChannel channel, User user) throws IOException {
        byte[] bytes = JacksonUtils.serialize(user).getBytes(StandardCharsets.UTF_8);
        MappedByteBuffer map = channel.map(FileChannel.MapMode.READ_WRITE, 0, bytes.length);
        long start = System.currentTimeMillis();
        map.put(bytes);
        log.info("开始刷盘");
        map.get();
        map.force();
        long time = System.currentTimeMillis() - start;
        log.info("刷盘结束：{}", time);
    }


    public static void main(String[] args) throws IOException, InterruptedException {
        RandomAccessFile aFile = new RandomAccessFile("/Users/southwood/channel.txt", "rw");
        FileChannel channel = aFile.getChannel();
        User user = new User();
        user.setId(0L);
        List<UserItem> itemList = new ArrayList<>(1000000);
        for (int i = 0; i < 1000000; i++) {
            UserItem userItem = new UserItem();
            userItem.setName("nimad");
            userItem.setAddress("kew");
            itemList.add(userItem);
        }
        user.setUserItemList(itemList);
//        write(channel, user);
//        read(channel);
//        readAndWrite(channel);
        force(channel, user);
        aFile.close();
    }

}

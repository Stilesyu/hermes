package com.github.www.hermes.config;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author Stiles yu
 * @since 1.0
 */
@Data
@Accessors(chain = true)
public class NettyConfig {

    private boolean userEpoll = false;
    private int workThreadSize = 5;
    private int port = 9607;


}

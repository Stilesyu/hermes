package com.github.www.hermes;

import org.junit.jupiter.api.Test;

/**
 * @author Stiles yu
 * @since 1.0
 */
public class NettyTest {


    @Test
    public void client() throws InterruptedException {
        ClientApplication clientApplication = new ClientApplication();
        clientApplication.open();
    }

    @Test
    public void server() throws InterruptedException {
        ServerApplication serverApplication = new ServerApplication();
        serverApplication.open();
    }

}

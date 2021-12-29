package com.github.www.hermes;


public interface Application {

    /**
     * open app
     *
     * @author Stilesyu
     * @since 1.0
     */
    void open() throws InterruptedException;


    /**
     * close app
     *
     * @author Stilesyu
     * @since 1.0
     */
    void close();

}

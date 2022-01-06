package com.github.www.hermes;


public interface Application {

    /**
     * start app
     *
     * @author Stilesyu
     * @since 1.0
     */
    void start() throws InterruptedException;


    /**
     * close app
     *
     * @author Stilesyu
     * @since 1.0
     */
    void close();

}

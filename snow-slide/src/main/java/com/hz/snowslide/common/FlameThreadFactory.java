package com.hz.snowslide.common;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>Package:com.hz.snowslide.common</p>
 * <p>Description: </p>
 * <p>Company: com.dfire</p>
 *
 * @author baiyundou
 * @date 2020/4/21 6:09
 */
public class FlameThreadFactory implements ThreadFactory {

    private static final String SEPARATOR = ":";

    private AtomicInteger COUNT = new AtomicInteger(0);

    private String name;

    public FlameThreadFactory(String name) {
        this.name = name;
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r);
        thread.setName(name + SEPARATOR + COUNT.getAndIncrement());
        return thread;
    }

}

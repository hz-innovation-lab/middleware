package com.hz.magic.component;

/**
 * <p>Package:com.hz.magic.component</p>
 * <p>Description: </p>
 * <p>Company: com.2dfire</p>
 *
 * @author baiyundou
 * @date 2020/4/21 15:58
 */
public class WorkerLock {

    private volatile boolean stop;

    public boolean isStop() {
        return stop;
    }

    public void setStop(boolean stop) {
        this.stop = stop;
    }

}

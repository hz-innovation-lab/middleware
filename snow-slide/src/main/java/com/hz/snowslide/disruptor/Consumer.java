package com.hz.snowslide.disruptor;

/**
 * <p>Package:com.hz.snowslide.component.producer</p>
 * <p>Description: </p>
 * <p>Company: com.dfire</p>
 *
 * @author baiyundou
 * @date 2020/4/21 4:52
 */


import com.hz.magic.component.WorkHandler;
import com.hz.magic.component.WorkerLock;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.atomic.AtomicLong;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Consumer implements WorkHandler<LongEvent> {

    private AtomicLong count = new AtomicLong(0);

    /**
     * 没必要用阻塞队列,试试
     */
    private SynchronousQueue<Long> synchronousQueue = new SynchronousQueue<>();

    private final WorkerLock workerLock = new WorkerLock();

    private List<Long> list = new ArrayList<>();

    @Override
    public WorkerLock onEvent(LongEvent longEvent) {
        //留一个跟ringBuffer玩阻塞,避免空转
        if (count.get() <= 0) {
            this.workerLock.setStop(true);
        } else {
            list.add(longEvent.getValue());
            count.decrementAndGet();
            if (count.get() == 0) {
                this.workerLock.setStop(true);
            }
        }

        return this.workerLock;
    }

    public List<Long> getBatchIds(long size) {
        while (workerLock.isStop()) {
            count.set(size);
            synchronized (workerLock) {
                workerLock.notify();
            }
        }
        while (!workerLock.isStop()) {
            //自旋即可
        }
        ArrayList<Long> result = new ArrayList<>(list);
        list.clear();
        return result;
    }


}
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
import java.util.stream.IntStream;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Consumer implements WorkHandler<LongEvent> {

    private AtomicLong count = new AtomicLong(0);

    /**
     * 没必要用阻塞队列,试试
     */
    private SynchronousQueue<Long> synchronousQueue = new SynchronousQueue<>();

    private final WorkerLock workerLock = new WorkerLock();

    private volatile List<Long> list = new ArrayList<>();

    @Override
    public WorkerLock onEvent(LongEvent longEvent) {
        //留一个跟ringBuffer玩阻塞,避免空转
        if (count.get() <= 0) {
            this.workerLock.setStop(true);
        } else {
            produceVolatile(longEvent);
            count.decrementAndGet();
            if (count.get() == 0) {
                this.workerLock.setStop(true);
            }
        }
        return this.workerLock;
    }

    private void produce(LongEvent longEvent) {
        try {
            synchronousQueue.put(longEvent.getValue());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void produceVolatile(LongEvent longEvent) {
        list.add(longEvent.getValue());
//        11
/*        if(list.size() + count.get() != 11){
            System.out.println(list.toString());
            System.out.println(count.get() + "  ----  " + list);
        }*/
    }

    public List<Long> getBatchIds(long size) {
        count.set(size);
        while (workerLock.isStop()) {
            synchronized (workerLock) {
                workerLock.notify();
            }
        }
        return consumeVolatile(size);
    }

    private List<Long> consume(Long size) {
/*
        ArrayList<Long> result = new ArrayList<>(list);
        list = new ArrayList<>();
*/

        List<Long> result = new ArrayList<>(size.intValue());
        IntStream.range(0, size.intValue()).forEach(i -> {
            try {
                result.add(synchronousQueue.take());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        return result;
    }

    private List<Long> consumeVolatile(Long size) {
        while (!workerLock.isStop()) ;
        //自旋即可
        //这个地方应该是单线程运行的
        ArrayList<Long> result = new ArrayList<>(list);
        list = new ArrayList<>();
        return result;
    }


}
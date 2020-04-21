package com.hz.snowslide.disruptor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.IntStream;

/**
 * <p>Package:com.hz.snowslide.disruptor</p>
 * <p>Description: </p>
 * <p>Company: com.dfire</p>
 *
 * @author baiyundou
 * @date 2020/4/21 5:01
 */
public class ConsumerPool {

    private List<Consumer> consumerList;

    private LinkedBlockingQueue<Consumer> consumerQueue = new LinkedBlockingQueue<>();

    public ConsumerPool(int consumerSize) {
        ArrayList<Consumer> list = new ArrayList<>(consumerSize);
        IntStream.range(0, consumerSize).forEach(i -> {
                    Consumer consumer = new Consumer();
                    list.add(consumer);
                    consumerQueue.add(consumer);
                }
        );
        consumerList = Collections.unmodifiableList(list);
    }


    public Consumer borrowConsumer(){
        while (true){
            try {
                return consumerQueue.take();
            } catch (InterruptedException e) {
                //不允许interrupt,就是如此霸道
            }
        }
    }

    public void returnConsumer(Consumer consumer) {
        while (true){
            try {
                consumerQueue.put(consumer);
                return;
            } catch (InterruptedException e) {
                //不允许interrupt,就是如此霸道
            }
        }
    }

    public List<Consumer> getConsumerList() {

        return consumerList;
    }

}

package com.hz.snowslide.disruptor;


import com.hz.magic.component.disruptor.RingBuffer;

/**
 * <p>Package:com.hz.snowslide.component.producer</p>
 * <p>Description: </p>
 * <p>Company: com.dfire</p>
 *
 * @author baiyundou
 * @date 2020/4/21 4:44
 */
public class LongEventProducer {

    private final RingBuffer<LongEvent> ringBuffer;

    public LongEventProducer(RingBuffer<LongEvent> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    /**
     * onData用来发布事件，每调用一次就发布一次事件事件
     * 它的参数会通过事件传递给消费者
     */
    public void onData(Long current) {
        long sequence = ringBuffer.next(); // Grab the next sequence
        try {
            LongEvent event = ringBuffer.get(sequence); // Get the entry in the Disruptor for the sequence
            event.set(current); // Fill with data
        } finally {
            ringBuffer.publish(sequence);
        }
    }

}
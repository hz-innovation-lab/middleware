package com.hz.snowslide.config;

import com.hz.magic.component.disruptor.BlockingWaitStrategy;
import com.hz.magic.component.disruptor.RingBuffer;
import com.hz.magic.component.disruptor.dsl.Disruptor;
import com.hz.magic.component.disruptor.dsl.ProducerType;
import com.hz.snowslide.common.FlameThreadFactory;
import com.hz.snowslide.component.flake.Cursor;
import com.hz.snowslide.component.flake.Flake;
import com.hz.snowslide.component.producer.BatchIdProducer;
import com.hz.snowslide.component.producer.SingleIdProducer;
import com.hz.snowslide.disruptor.Consumer;
import com.hz.snowslide.disruptor.ConsumerPool;
import com.hz.snowslide.disruptor.LongEvent;
import com.hz.snowslide.disruptor.LongEventFactory;
import com.hz.snowslide.disruptor.LongEventProducer;
import com.hz.snowslide.disruptor.SnowSlideExceptionHandler;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * <p>Package:com.hz.snowslide.service</p>
 * <p>Description: </p>
 * <p>Company: com.dfire</p>
 *
 * @author baiyundou
 * @date 2020/4/11 7:26
 */
@Configuration
public class FlameConfiguration {

    /*
        分布式的时候 游标是要被同步到zk上面的.新的node的游标从zk上面取.workId也是从zk上面取
     */
    private long currentMills = System.currentTimeMillis();

    @Bean
    public SingleIdProducer singleIdProducer(Flake flake) {
        return new SingleIdProducer(new Cursor(currentMills), flake);
    }

    @Bean
    public BatchIdProducer batchIdProducer(Flake flake) {
        return new BatchIdProducer(new Cursor(currentMills), flake);
    }

    @Bean
    public ConsumerPool consumerPool(@Value("${disruptor.consumerSize}") int consumerSize) {
        return new ConsumerPool(consumerSize);
    }

    @Bean
    public ThreadPoolExecutor consumerThreadPoolExecutors() {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(15, 20, 2, TimeUnit.SECONDS, new LinkedBlockingQueue<>(10), new FlameThreadFactory("线程"), new ThreadPoolExecutor.AbortPolicy());
        return threadPoolExecutor;
    }

    @Bean
    public Disruptor disruptor(ConsumerPool consumerPool, BatchIdProducer batchIdProducer, @Value("${ringBuffer.maxSize}") int ringBufferSize, ThreadPoolExecutor threadPoolExecutor) {
        LongEventFactory eventFactory = new LongEventFactory();
        Disruptor<LongEvent> disruptor = new Disruptor<>(eventFactory, ringBufferSize, threadPoolExecutor,
                ProducerType.SINGLE, new BlockingWaitStrategy());
        RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();
        // 创建n个消费者来处理同一个生产者发的消息
        Consumer[] consumers = new Consumer[consumerPool.getConsumerList().size()];
        consumerPool.getConsumerList().toArray(consumers);
        disruptor.handleExceptionsWith(new SnowSlideExceptionHandler());
        disruptor.handleEventsWithWorkerPool(consumers);
        disruptor.start();
        LongEventProducer longEventProducer = new LongEventProducer(ringBuffer);
        threadPoolExecutor.execute(() -> {
            while (true) {
                long id = batchIdProducer.getId();
                longEventProducer.onData(id);
            }
        });
        return disruptor;
    }

}

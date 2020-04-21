package com.hz.snowslide.service.impl;

import com.hz.snowslide.common.Constant;
import com.hz.snowslide.common.Result;
import com.hz.snowslide.common.ResultUtil;
import com.hz.snowslide.component.consumer.SingleIdConsumer;
import com.hz.snowslide.disruptor.Consumer;
import com.hz.snowslide.disruptor.ConsumerPool;
import com.hz.snowslide.service.UniqueIdGenerator;
import com.google.common.collect.Lists;

import org.springframework.stereotype.Service;

import java.util.List;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>Package:com.hz.snowslide.service</p>
 * <p>Description: </p>
 * <p>Company: com.dfire</p>
 *
 * @author baiyundou
 * @date 2020/4/11 7:33
 */
@Service
@Slf4j
public class UniqueIdGeneratorImpl implements UniqueIdGenerator {

    @Resource
    private SingleIdConsumer singleIdProducer;

    @Resource
    private ConsumerPool consumerPool;

    @Override
    public Result<Long> nextId() {
        return ResultUtil.successResult(singleIdProducer.getId());
    }

    @Override
    public Result<List<Long>> nextBatchId(int size) {
        if (size <= 0) {
            return ResultUtil.failResult("size 不能小于1");
        }
        if (size > Constant.MAX_BATCH_SIZE) {
            return ResultUtil.failResult("size 不能大于" + Constant.MAX_BATCH_SIZE);
        }
        return ResultUtil.successResult(batchId(size));
    }


    private List<Long> batchId(int size) {
        Consumer consumer = null;
        try {
            consumer = consumerPool.borrowConsumer();
            return consumer.getBatchIds(size);
        } catch (Exception e) {
            return Lists.newArrayList(singleIdProducer.getId());
            //失败了,所以返回一个singleId
        } finally {
            if(consumer != null){
                consumerPool.returnConsumer(consumer);
            }
        }
    }

    @Override
    public Result<Long> fixId() {
        return null;
    }


}

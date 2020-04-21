package com.hz.snowslide.controller;


import com.hz.snowslide.common.FlameThreadFactory;
import com.hz.snowslide.service.UniqueIdGenerator;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import javax.annotation.Resource;

/**
 * <p>Package:com.hz.snowslide.controller</p>
 * <p>Description: </p>
 * <p>Company: com.dfire</p>
 *
 * @author baiyundou
 * @date 2020/4/11 7:53
 */
@RestController
@RequestMapping("/ids")
public class IdController {

    @Resource
    private UniqueIdGenerator uniqueIdGenerator;

    @RequestMapping("/batch/{size}")
    public String batch(@PathVariable("size") int size) {
        List<Long> result = uniqueIdGenerator.nextBatchId(size).getModel();
        StringBuilder s = new StringBuilder();
        result.forEach(e -> {
            s.append(e);
            s.append("\r\n");
        });
        return s.toString();
    }

    @RequestMapping("/test/{size}")
    public Long test(@PathVariable("size") int size) {
        long l = System.currentTimeMillis();
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(15, 20, 2, TimeUnit.SECONDS, new LinkedBlockingQueue<>(10), new FlameThreadFactory("test线"), new ThreadPoolExecutor.AbortPolicy());
        CompletionService<Object> completionService = new ExecutorCompletionService<>(threadPoolExecutor);
        IntStream.range(0, size).forEach(i -> {
            completionService.submit(() -> {
                List<Long> result = uniqueIdGenerator.nextBatchId(1000).getModel();
                return result;
            });
        });
        IntStream.range(0, size).forEach(i -> {
            try {
                completionService.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        threadPoolExecutor.shutdown();
        long l1 = System.currentTimeMillis();
        return l1 - l;
    }
}

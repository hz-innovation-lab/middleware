package com.hz.snowslide;

import com.hz.snowslide.controller.IdController;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

@SpringBootTest
class SnowSlideApplicationTests {

    @Resource
    private IdController idController;

    @Test
    void contextLoads() {
        int size = 10;
        List<Long> list = null;
        while (true){
            try {
                TimeUnit.MILLISECONDS.sleep(10l);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            List<Long> batch = idController.batch(size);
            if(batch.size() != size){
                System.out.println(list);
                System.out.println("----------------");
                System.out.println(batch);
            }
            list = batch;

        }

    }

}

package com.hz.snowslide;

import com.hz.snowslide.controller.IdController;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

@SpringBootTest
class SnowSlideApplicationTests {

    @Resource
    private IdController idController;

    @Test
    void contextLoads() {
        while (true){
            try {
                TimeUnit.MILLISECONDS.sleep(200L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            List<Long> batch = idController.batch(10);
            if(batch.size() == 20){
                System.out.println(batch);
            }
        }

    }

}

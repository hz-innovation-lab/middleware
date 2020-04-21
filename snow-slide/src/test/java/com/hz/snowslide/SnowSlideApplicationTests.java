package com.hz.snowslide;

import com.hz.snowslide.controller.IdController;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

@SpringBootTest
class SnowSlideApplicationTests {

    @Resource
    private IdController idController;

    @Test
    void contextLoads() {
        int size = 10;
        Set<Long> set = new HashSet<>();
        while (true){
            try {
                TimeUnit.MILLISECONDS.sleep(10L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            List<Long> batch = idController.batch(size);
            batch.forEach(e->{
                if(!set.add(e)){
                    System.out.println("cry");
                }
            });
        }

    }

}

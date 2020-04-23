package com.hz.snowslide;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
/**
 * 雪花算法+disruptor(雷电)
 * 故名雪崩
 */
public class SnowSlideApplication {

    public static void main(String[] args) {
        SpringApplication.run(SnowSlideApplication.class, args);
    }

}

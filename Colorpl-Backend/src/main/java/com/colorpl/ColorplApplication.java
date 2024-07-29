package com.colorpl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class ColorplApplication {

    public static void main(String[] args) {
        SpringApplication.run(ColorplApplication.class, args);
    }

}

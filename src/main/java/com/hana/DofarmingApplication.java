package com.hana;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DofarmingApplication {

    public static void main(String[] args) {
        SpringApplication.run(DofarmingApplication.class, args);
    }

}

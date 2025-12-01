package com.smalldogg.whereiam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class WhereIAmApplication {

    public static void main(String[] args) {
        SpringApplication.run(WhereIAmApplication.class, args);
    }

}

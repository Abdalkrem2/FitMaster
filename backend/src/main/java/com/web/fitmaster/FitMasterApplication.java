package com.web.fitmaster;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class FitMasterApplication {

    public static void main(String[] args) {
        SpringApplication.run(FitMasterApplication.class, args);
    }

}

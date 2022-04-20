package com.example.fabnavrest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
public class FabNavRestApplication {

    public static void main(String[] args) {
        SpringApplication.run(FabNavRestApplication.class, args);
    }

}

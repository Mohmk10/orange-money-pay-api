package com.orangemoney.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class OmPayApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(OmPayApiApplication.class, args);
    }
}

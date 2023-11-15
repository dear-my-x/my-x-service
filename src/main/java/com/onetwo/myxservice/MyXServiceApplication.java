package com.onetwo.myxservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MyXServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyXServiceApplication.class, args);
    }

}

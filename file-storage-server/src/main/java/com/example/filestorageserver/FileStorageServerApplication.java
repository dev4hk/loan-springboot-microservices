package com.example.filestorageserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class FileStorageServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(FileStorageServerApplication.class, args);
    }

}

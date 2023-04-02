package com.molcom.nms;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class NmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(NmsApplication.class, args);
    }
    @Bean
    public ObjectMapper getObjectMapper() {
        return new ObjectMapper();
    }

}

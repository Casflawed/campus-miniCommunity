package com.flameking;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;


import javax.annotation.PostConstruct;

import java.util.TimeZone;

@MapperScan(value = "com.flameking.mapper")
@EnableAsync
@SpringBootApplication
public class RetwisapiApplication {

    public static void main(String[] args) {
        SpringApplication.run(RetwisapiApplication.class, args);
    }
    @PostConstruct
    void setDefaultTimezone() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
    }
}

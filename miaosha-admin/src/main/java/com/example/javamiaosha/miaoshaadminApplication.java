package com.example.javamiaosha;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = {"com.example.generator.mapper","com.example.javamiaosha.dao"})
public class miaoshaadminApplication {

    public static void main(String[] args) {
        SpringApplication.run(miaoshaadminApplication.class, args);
    }

}

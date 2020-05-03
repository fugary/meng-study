package com.mengstudy.simple.tx.db;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.mengstudy.simple.tx.db.mapper")
@SpringBootApplication
public class SimpleBootTxDbApplication {

    public static void main(String[] args) {
        SpringApplication.run(SimpleBootTxDbApplication.class, args);
    }

}

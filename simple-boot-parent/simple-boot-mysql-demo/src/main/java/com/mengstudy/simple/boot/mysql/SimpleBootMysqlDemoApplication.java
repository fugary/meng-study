package com.mengstudy.simple.boot.mysql;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.mengstudy.simple.boot.mysql.mapper")
@SpringBootApplication
public class SimpleBootMysqlDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SimpleBootMysqlDemoApplication.class, args);
	}

}

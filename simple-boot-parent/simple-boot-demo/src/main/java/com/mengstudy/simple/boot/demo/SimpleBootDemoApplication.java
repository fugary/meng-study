package com.mengstudy.simple.boot.demo;

import com.mengstudy.simple.boot.demo.entity.User;
import com.mengstudy.simple.boot.demo.mapper.UserMapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@MapperScan("com.mengstudy.simple.boot.demo.mapper")
@SpringBootApplication
public class SimpleBootDemoApplication {

	@Autowired
	private UserMapper userMapper;

	public static void main(String[] args) {
		SpringApplication.run(SimpleBootDemoApplication.class, args);
	}

	@GetMapping("/hello")
	public String hello(){
		return "Hello World!";
	}

	@GetMapping("/users")
	public List<User> users(){
		return userMapper.selectList(null);
	}

}

package com.mengstudy.boot.tx.saga.demo;

import com.mengstudy.boot.tx.saga.demo.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created on 2021/9/23 16:14 .<br>
 *
 * @author gary.fu
 */
@RequestMapping("/")
@RestController
@SpringBootApplication
public class SimpleBootSagaDemoApplication {

    @Autowired
    private DemoService demoService;

    @GetMapping("/hello")
    public String hello(){
        return demoService.doSomething();
    }

    public static void main(String[] args) {
        SpringApplication.run(SimpleBootSagaDemoApplication.class, args);
    }

}

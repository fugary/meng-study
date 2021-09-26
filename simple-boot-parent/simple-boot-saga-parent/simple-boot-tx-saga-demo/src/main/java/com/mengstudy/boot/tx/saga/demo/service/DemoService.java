package com.mengstudy.boot.tx.saga.demo.service;

import com.mengstudy.boot.tx.saga.annotation.SimpleTransactional;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * @author Gary Fu
 * @date 2021/9/25 14:45
 */
@Component
public class DemoService {

    @Getter
    @Setter(onMethod_ = {@Autowired})
    private DemoService1 demoService1;
    @Getter
    @Setter(onMethod_ = {@Autowired})
    private DemoService2 demoService2;

    @SimpleTransactional
    public String doSomething() {
        demoService1.demo1("test");
        demoService2.demo2();
        boolean error = new Random().nextBoolean();
        if (error) {
            throw new RuntimeException("test");
        }
        return "hello world";
    }
}

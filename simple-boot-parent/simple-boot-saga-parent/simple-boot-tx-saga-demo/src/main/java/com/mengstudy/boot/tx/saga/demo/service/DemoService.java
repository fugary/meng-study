package com.mengstudy.boot.tx.saga.demo.service;

import com.mengstudy.boot.tx.saga.annotation.SimpleTransactional;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Gary Fu
 * @date 2021/9/25 14:45
 */
@Component
public class DemoService {

    @Autowired
    @Getter
    @Setter
    private DemoService1 demoService1;
    @Autowired
    @Getter
    @Setter
    private DemoService2 demoService2;

    @SimpleTransactional
    public String doSomething() {
        demoService1.demo1();
        demoService2.demo2();
        return null;
    }
}

package com.mengstudy.boot.tx.saga.demo.service;

import com.mengstudy.boot.tx.saga.annotation.SimpleSaga;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author Gary Fu
 * @date 2021/9/25 14:48
 */
@Component
public class DemoService1 {

    public static final Logger LOGGER = LoggerFactory.getLogger(DemoService1.class);

    @SimpleSaga(cancelMethod = "cancelDemo1")
    public void demo1(){
        LOGGER.info("demo1................");
    }

    public void cancelDemo1(){
        LOGGER.info("cancel demo1");
    }
}

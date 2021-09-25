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
public class DemoService2 {

    public static final Logger LOGGER = LoggerFactory.getLogger(DemoService2.class);

    @SimpleSaga(cancelMethod = "cancelDemo2")
    public void demo2(){
        LOGGER.info("demo2................");
    }

    public void cancelDemo2(){
        LOGGER.info("cancel demo2");
    }
}

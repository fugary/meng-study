package com.mengstudy.boot.tx.saga.demo.service;

import com.mengstudy.boot.tx.saga.annotation.SimpleSaga;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author Gary Fu
 * @date 2021/9/25 14:48
 */
@Component
@Slf4j
public class DemoService1 {

    @SimpleSaga(cancelMethod = "cancelDemo1")
    public void demo1(){
        log.info("demo1................");
    }

    public void cancelDemo1(){
        log.info("cancel demo1");
    }
}

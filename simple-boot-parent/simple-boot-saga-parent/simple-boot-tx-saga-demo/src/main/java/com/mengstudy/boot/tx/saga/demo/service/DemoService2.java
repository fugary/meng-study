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
public class DemoService2 {

    @SimpleSaga(cancelMethod = "cancelDemo2")
    public void demo2(){
        log.info("demo2................");
    }

    public void cancelDemo2(){
        log.info("cancel demo2");
    }
}

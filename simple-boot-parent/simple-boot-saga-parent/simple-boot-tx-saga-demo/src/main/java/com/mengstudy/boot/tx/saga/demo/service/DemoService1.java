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
    public void demo1(String input){
        log.info("execute demo1................" + input);
    }

    public void cancelDemo1(String input){
        log.info("cancel demo1................." + input);
    }
}

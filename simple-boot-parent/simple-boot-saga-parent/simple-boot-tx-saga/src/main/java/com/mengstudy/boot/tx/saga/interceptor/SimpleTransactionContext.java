package com.mengstudy.boot.tx.saga.interceptor;

import com.mengstudy.boot.tx.saga.dto.SagaSimpleTransaction;
import lombok.Data;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created on 2021/9/23 16:27 .<br>
 *
 * @author gary.fu
 */
@Data
public class SimpleTransactionContext {

    private SagaSimpleTransaction transaction;

    private AtomicInteger subIndex = new AtomicInteger(0);
}

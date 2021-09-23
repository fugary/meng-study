package com.mengstudy.boot.tx.saga.interceptor;

import lombok.Data;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created on 2021/9/23 16:27 .<br>
 *
 * @author gary.fu
 */
@Data
public class SimpleTransactionContext {

    private String txId;

    private Date startDate;

    private Date endDate;

    private Integer status;

    private String message;

    private AtomicInteger subIndex = new AtomicInteger(0);
}

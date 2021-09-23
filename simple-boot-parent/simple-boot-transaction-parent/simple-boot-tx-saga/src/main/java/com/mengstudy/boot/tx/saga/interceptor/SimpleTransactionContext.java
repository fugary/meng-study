package com.mengstudy.boot.tx.saga.interceptor;

import lombok.Data;

import java.util.Date;

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
}

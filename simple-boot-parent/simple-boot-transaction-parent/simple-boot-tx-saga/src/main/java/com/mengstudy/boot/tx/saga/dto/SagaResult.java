package com.mengstudy.boot.tx.saga.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * Created on 2021/9/23 16:18 .<br>
 *
 * @author gary.fu
 */
@Data
public class SagaResult implements Serializable {

    private boolean success;

    private String message;
}

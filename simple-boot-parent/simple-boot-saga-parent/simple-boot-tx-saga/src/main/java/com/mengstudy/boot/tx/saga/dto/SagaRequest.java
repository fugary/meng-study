package com.mengstudy.boot.tx.saga.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * Created on 2021/9/23 16:20 .<br>
 *
 * @author gary.fu
 */
@Data
public class SagaRequest implements Serializable {

    private String txId;

    private String subTxId;

    private String serviceName;

    private String serviceClazz;

    private String paramClazz;

    private String paramData;

    private String cancelMethod;

}

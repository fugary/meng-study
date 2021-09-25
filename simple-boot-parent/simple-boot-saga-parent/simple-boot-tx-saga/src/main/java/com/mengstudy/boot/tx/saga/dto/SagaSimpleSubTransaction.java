package com.mengstudy.boot.tx.saga.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Gary Fu
 * @date 2021/9/25 19:33
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SagaSimpleSubTransaction extends BaseSimpleTransaction {

    private String subTxId;

    private Integer idxNo;

    private String serviceName;

    private String serviceClazz;

    private String methodKey;

    private String paramClazz;

    private String paramData;

    private String cancelMethod;

}

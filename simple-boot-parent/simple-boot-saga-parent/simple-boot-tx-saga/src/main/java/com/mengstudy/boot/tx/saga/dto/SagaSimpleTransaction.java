package com.mengstudy.boot.tx.saga.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created on 2021/9/24 13:22 .<br>
 *
 * @author gary.fu
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SagaSimpleTransaction extends BaseSimpleTransaction {

    private Integer retryTimes = 0;
}

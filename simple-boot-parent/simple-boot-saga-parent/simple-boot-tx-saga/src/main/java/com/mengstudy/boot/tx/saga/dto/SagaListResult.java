package com.mengstudy.boot.tx.saga.dto;

import lombok.Data;

import java.util.List;

/**
 * Created on 2021/9/24 13:21 .<br>
 *
 * @author gary.fu
 */
@Data
public class SagaListResult extends SagaResult {

    private List<SagaSimpleTransaction> transactions;

}

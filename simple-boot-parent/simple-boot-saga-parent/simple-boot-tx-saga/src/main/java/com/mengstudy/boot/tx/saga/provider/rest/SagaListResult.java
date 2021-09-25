package com.mengstudy.boot.tx.saga.provider.rest;

import com.mengstudy.boot.tx.saga.dto.SagaSimpleTransaction;
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

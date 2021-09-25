package com.mengstudy.boot.tx.saga.provider.rest;

import com.mengstudy.boot.tx.saga.dto.SagaSimpleSubTransaction;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created on 2021/9/23 16:20 .<br>
 *
 * @author gary.fu
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SagaSubRequest extends SagaRequest {

    private SagaSimpleSubTransaction subTransaction;

}

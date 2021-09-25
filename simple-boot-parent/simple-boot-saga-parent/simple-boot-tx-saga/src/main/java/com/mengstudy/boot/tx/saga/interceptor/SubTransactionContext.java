package com.mengstudy.boot.tx.saga.interceptor;

import com.mengstudy.boot.tx.saga.dto.SagaSimpleSubTransaction;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created on 2021/9/23 17:28 .<br>
 *
 * @author gary.fu
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SubTransactionContext extends SimpleTransactionContext {

    private SagaSimpleSubTransaction sagaTransaction;

}

package com.mengstudy.boot.tx.saga.cancel;

import com.mengstudy.boot.tx.saga.dto.SagaSimpleSubTransaction;
import com.mengstudy.boot.tx.saga.dto.SagaSimpleTransaction;

import java.util.List;

/**
 * Created on 2021/9/26 9:50 .<br>
 *
 * @author gary.fu
 */
public interface SimpleTransactionCancelProvider {

    /**
     * 回滚主事务
     *
     * @param transaction
     * @param subTransactions
     */
    boolean cancelSimpleTransaction(SagaSimpleTransaction transaction, List<SagaSimpleSubTransaction> subTransactions);

    /**
     * 回滚子事务
     *
     * @param transaction
     * @param simpleSubTransaction
     */
    boolean cancelSubTransaction(SagaSimpleTransaction transaction, SagaSimpleSubTransaction simpleSubTransaction);

}

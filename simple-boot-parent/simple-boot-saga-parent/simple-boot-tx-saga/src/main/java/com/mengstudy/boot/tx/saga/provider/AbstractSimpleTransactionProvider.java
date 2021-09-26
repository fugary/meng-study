package com.mengstudy.boot.tx.saga.provider;

import com.mengstudy.boot.tx.saga.cancel.SimpleTransactionCancelProvider;
import com.mengstudy.boot.tx.saga.dto.SagaSimpleSubTransaction;
import com.mengstudy.boot.tx.saga.dto.SagaSimpleTransaction;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Created on 2021/9/26 10:07 .<br>
 *
 * @author gary.fu
 */
@Slf4j
public abstract class AbstractSimpleTransactionProvider implements SimpleTransactionProvider {

    @Getter
    @Setter
    protected SimpleTransactionCancelProvider simpleTransactionCancelProvider;

    protected boolean cancelSimpleTransaction(SagaSimpleTransaction transaction, List<SagaSimpleSubTransaction> subTransactions) {
        boolean result = simpleTransactionCancelProvider.cancelSimpleTransaction(transaction, subTransactions);
        this.updateSimpleTransaction(transaction);
        return result;
    }
}

package com.mengstudy.boot.tx.saga.provider;

import com.mengstudy.boot.tx.saga.cancel.SimpleTransactionCancelProvider;
import com.mengstudy.boot.tx.saga.constant.SimpleTransactionConstant;
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
        boolean result = false;
        try {
            result = simpleTransactionCancelProvider.cancelSimpleTransaction(transaction, subTransactions);
        } catch (Exception e) {
            if (transaction.getRetryTimes() < simpleTransactionCancelProvider.getRetryTimes()) {
                transaction.setStatus(SimpleTransactionConstant.STATUS_CANCEL_FAILED0);
            } else {
                transaction.setStatus(SimpleTransactionConstant.STATUS_CANCEL_FAILED);
            }
            transaction.setMessage(e.getMessage());
            this.updateSimpleTransaction(transaction);
        }
        return result;
    }
}

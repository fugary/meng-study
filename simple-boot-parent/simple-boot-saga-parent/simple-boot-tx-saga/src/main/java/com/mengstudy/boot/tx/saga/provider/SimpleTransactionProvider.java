package com.mengstudy.boot.tx.saga.provider;

import com.mengstudy.boot.tx.saga.dto.SagaSimpleTransaction;
import com.mengstudy.boot.tx.saga.interceptor.SimpleTransactionContext;
import com.mengstudy.boot.tx.saga.interceptor.SubTransactionContext;

import java.util.List;

/**
 * Created on 2021/9/23 16:54 .<br>
 *
 * @author gary.fu
 */
public interface SimpleTransactionProvider {

    void startSimpleTransaction(SimpleTransactionContext context);

    void endSimpleTransaction(SimpleTransactionContext context);

    void recordSubTransaction(SubTransactionContext transaction);

    List<SagaSimpleTransaction> loadFailed(List<String> keys);

    void cancelSimpleTransaction(SagaSimpleTransaction simpleTransaction);

    void updateSimpleTransaction(SagaSimpleTransaction simpleTransaction);
}

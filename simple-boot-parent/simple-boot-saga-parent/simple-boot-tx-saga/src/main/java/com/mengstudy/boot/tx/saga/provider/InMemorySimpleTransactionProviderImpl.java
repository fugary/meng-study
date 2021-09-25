package com.mengstudy.boot.tx.saga.provider;

import com.mengstudy.boot.tx.saga.dto.SagaSimpleTransaction;
import com.mengstudy.boot.tx.saga.interceptor.SimpleTransactionContext;
import com.mengstudy.boot.tx.saga.interceptor.SubTransaction;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Gary Fu
 * @date 2021/9/25 14:53
 */
public class InMemorySimpleTransactionProviderImpl implements SimpleTransactionProvider{

    private Map<String, SagaSimpleTransaction> transactionMap = new ConcurrentHashMap<>();

    @Override
    public void startSimpleTransaction(SimpleTransactionContext context) {

    }

    @Override
    public void endSimpleTransaction(SimpleTransactionContext context) {

    }

    @Override
    public void recordSubTransaction(SubTransaction transaction) {

    }

    @Override
    public List<SagaSimpleTransaction> loadFailed(List<String> keys) {
        return null;
    }
}

package com.mengstudy.boot.tx.saga.provider.memory;

import com.mengstudy.boot.tx.saga.dto.SagaSimpleTransaction;
import com.mengstudy.boot.tx.saga.interceptor.SimpleTransactionContext;
import com.mengstudy.boot.tx.saga.interceptor.SubTransactionContext;
import com.mengstudy.boot.tx.saga.provider.SimpleTransactionProvider;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static com.mengstudy.boot.tx.saga.interceptor.SimpleTransactionUtils.*;

/**
 * @author Gary Fu
 * @date 2021/9/25 14:53
 */
public class InMemorySimpleTransactionProviderImpl implements SimpleTransactionProvider {

    private Map<String, MemorySimpleTransaction> transactionMap = new ConcurrentHashMap<>();

    @Override
    public void startSimpleTransaction(SimpleTransactionContext context) {
        MemorySimpleTransaction simpleTransaction = new MemorySimpleTransaction();
        simpleTransaction.setTransaction(context.getTransaction());
        transactionMap.put(context.getTransaction().getTxId(), simpleTransaction);
    }

    @Override
    public void endSimpleTransaction(SimpleTransactionContext context) {
        String txId = context.getTransaction().getTxId();
        MemorySimpleTransaction simpleTransaction = transactionMap.get(txId);
        if (simpleTransaction != null) {
            simpleTransaction.getTransaction().setStatus(context.getTransaction().getStatus());
            simpleTransaction.getTransaction().setEndDate(context.getTransaction().getEndDate());
        }
    }

    @Override
    public void recordSubTransaction(SubTransactionContext context) {
        String txId = context.getTransaction().getTxId();
        MemorySimpleTransaction simpleTransaction = transactionMap.get(txId);
        if (simpleTransaction != null) {
            simpleTransaction.getSubTransactions().add(context.getSagaTransaction());
        }
    }

    @Override
    public List<SagaSimpleTransaction> loadFailed(List<String> keys) {
        transactionMap.entrySet().removeIf(entry -> Arrays.asList(STATUS_SUCCESS, STATUS_CANCELED)
                .contains(entry.getValue().getTransaction().getStatus()));
        return transactionMap.entrySet().stream().filter(entry -> Arrays.asList(STATUS_FAILED, STATUS_TIMEOUT)
                        .contains(entry.getValue().getTransaction().getStatus()))
                .map(entry -> entry.getValue().getTransaction()).collect(Collectors.toList());
    }

    @Override
    public void markTransactionCanceled(SimpleTransactionContext context) {
        String txId = context.getTransaction().getTxId();
        MemorySimpleTransaction simpleTransaction = transactionMap.get(txId);
        if (simpleTransaction != null) {
            simpleTransaction.getTransaction().setStatus(STATUS_CANCELED);
            simpleTransaction.getTransaction().setEndDate(new Date());
        }
    }
}

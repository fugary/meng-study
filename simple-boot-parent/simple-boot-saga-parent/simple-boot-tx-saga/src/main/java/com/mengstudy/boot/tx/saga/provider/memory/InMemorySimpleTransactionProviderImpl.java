package com.mengstudy.boot.tx.saga.provider.memory;

import com.mengstudy.boot.tx.saga.dto.SagaSimpleTransaction;
import com.mengstudy.boot.tx.saga.interceptor.SimpleTransactionContext;
import com.mengstudy.boot.tx.saga.interceptor.SimpleTransactionUtils;
import com.mengstudy.boot.tx.saga.interceptor.SubTransactionContext;
import com.mengstudy.boot.tx.saga.provider.AbstractSimpleTransactionProvider;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author Gary Fu
 * @date 2021/9/25 14:53
 */
@Slf4j
public class InMemorySimpleTransactionProviderImpl extends AbstractSimpleTransactionProvider {

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
        transactionMap.entrySet().removeIf(entry -> SimpleTransactionUtils.isTransactionEnded(entry.getValue().getTransaction()));
        return transactionMap.entrySet().stream()
                .filter(entry -> SimpleTransactionUtils.isTransactionNeedCancel(entry.getValue().getTransaction()))
                .map(entry -> entry.getValue().getTransaction()).collect(Collectors.toList());
    }

    @Override
    public void cancelSimpleTransaction(SagaSimpleTransaction transaction) {
        MemorySimpleTransaction memoryTransaction = transactionMap.get(transaction.getTxId());
        if (memoryTransaction != null) {
            super.cancelSimpleTransaction(transaction, memoryTransaction.getSubTransactions());
        }
    }

    @Override
    public void updateSimpleTransaction(SagaSimpleTransaction transaction) {
        String txId = transaction.getTxId();
        MemorySimpleTransaction simpleTransaction = transactionMap.get(txId);
        if (simpleTransaction != null) {
            simpleTransaction.getTransaction().setStatus(transaction.getStatus());
            simpleTransaction.getTransaction().setEndDate(new Date());
        }
    }
}

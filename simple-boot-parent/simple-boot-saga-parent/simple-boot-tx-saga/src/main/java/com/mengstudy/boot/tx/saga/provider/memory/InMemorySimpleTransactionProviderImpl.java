package com.mengstudy.boot.tx.saga.provider.memory;

import com.mengstudy.boot.tx.saga.dto.SagaSimpleSubTransaction;
import com.mengstudy.boot.tx.saga.dto.SagaSimpleTransaction;
import com.mengstudy.boot.tx.saga.interceptor.SimpleTransactionContext;
import com.mengstudy.boot.tx.saga.interceptor.SimpleTransactionUtils;
import com.mengstudy.boot.tx.saga.interceptor.SubTransactionContext;
import com.mengstudy.boot.tx.saga.meta.SimpleSagaMeta;
import com.mengstudy.boot.tx.saga.meta.SimpleTransactionMetaHelper;
import com.mengstudy.boot.tx.saga.provider.SimpleTransactionProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static com.mengstudy.boot.tx.saga.interceptor.SimpleTransactionUtils.*;

/**
 * @author Gary Fu
 * @date 2021/9/25 14:53
 */
public class InMemorySimpleTransactionProviderImpl implements SimpleTransactionProvider {

    public static final Logger LOGGER = LoggerFactory.getLogger(InMemorySimpleTransactionProviderImpl.class);

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
    public void cancelSimpleTransaction(SagaSimpleTransaction transaction) {
        MemorySimpleTransaction simpleTransaction = transactionMap.get(transaction.getTxId());
        if (simpleTransaction != null) {
            List<SagaSimpleSubTransaction> subTransactions = new ArrayList<>(simpleTransaction.getSubTransactions());
            subTransactions.sort(Comparator.comparing(SagaSimpleSubTransaction::getIdxNo).reversed());
            for (SagaSimpleSubTransaction subTransaction : subTransactions) {
                this.cancelSubTransaction(subTransaction);
            }
            this.markTransactionCanceled(transaction);
        }
    }

    protected void cancelSubTransaction(SagaSimpleSubTransaction subTransaction) {
        SimpleSagaMeta simpleSagaMeta = SimpleTransactionMetaHelper.getSageMeta(subTransaction.getTxKey(), subTransaction.getMethodKey());
        if (simpleSagaMeta != null) {
            Object targetBean = simpleSagaMeta.getTargetBean();
            try {
                Method method = Class.forName(simpleSagaMeta.getServiceClass()).getMethod(subTransaction.getCancelMethod());
                List<String> paramTypes = fromJson(subTransaction.getParamClazz(), List.class);
                List<String> paramDataList = fromJson(subTransaction.getParamData(), List.class);
                List<Object> params = new ArrayList<>();
                for (int i = 0; i < paramDataList.size(); i++) {
                    Class<?> paramClass = Class.forName(paramTypes.get(i));
                    params.add(fromJson(paramDataList.get(i), paramClass));
                }
                method.invoke(targetBean, params.toArray());
            } catch (NoSuchMethodException | ClassNotFoundException | IllegalAccessException | InvocationTargetException e) {
                LOGGER.error("查找方法错误", e);
            }
        }
    }

    @Override
    public void markTransactionCanceled(SagaSimpleTransaction transaction) {
        String txId = transaction.getTxId();
        MemorySimpleTransaction simpleTransaction = transactionMap.get(txId);
        if (simpleTransaction != null) {
            simpleTransaction.getTransaction().setStatus(STATUS_CANCELED);
            simpleTransaction.getTransaction().setEndDate(new Date());
        }
    }
}

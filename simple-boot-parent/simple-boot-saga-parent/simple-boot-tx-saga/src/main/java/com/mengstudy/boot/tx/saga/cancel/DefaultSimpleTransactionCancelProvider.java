package com.mengstudy.boot.tx.saga.cancel;

import com.mengstudy.boot.tx.saga.constant.SimpleTransactionConstant;
import com.mengstudy.boot.tx.saga.dto.SagaSimpleSubTransaction;
import com.mengstudy.boot.tx.saga.dto.SagaSimpleTransaction;
import com.mengstudy.boot.tx.saga.interceptor.SimpleTransactionUtils;
import com.mengstudy.boot.tx.saga.meta.SimpleSagaMeta;
import com.mengstudy.boot.tx.saga.meta.SimpleTransactionMetaHelper;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.mengstudy.boot.tx.saga.interceptor.SimpleTransactionUtils.fromJson;

/**
 * Created on 2021/9/26 9:51 .<br>
 *
 * @author gary.fu
 */
@Slf4j
@Getter
@Setter
public class DefaultSimpleTransactionCancelProvider implements SimpleTransactionCancelProvider {

    private Integer retryTimes = 3;

    @Override
    public boolean cancelSimpleTransaction(SagaSimpleTransaction transaction, List<SagaSimpleSubTransaction> sagaSubTransactions) {
        if (SimpleTransactionUtils.isTransactionEnded(transaction)) {
            return true;
        }
        List<SagaSimpleSubTransaction> subTransactions = new ArrayList<>(sagaSubTransactions);
        subTransactions.sort(Comparator.comparing(SagaSimpleSubTransaction::getIdxNo).reversed());
        boolean hasFailedTransaction = false;
        transaction.setRetryTimes(transaction.getRetryTimes() + 1);
        for (SagaSimpleSubTransaction subTransaction : subTransactions) {
            boolean subSuccess = this.cancelSubTransaction(transaction, subTransaction);
            if (!subSuccess) {
                hasFailedTransaction = true;
            }
        }
        if (hasFailedTransaction) {
            if (transaction.getRetryTimes() < getRetryTimes()) {
                transaction.setStatus(SimpleTransactionConstant.STATUS_CANCEL_FAILED0);
            } else {
                transaction.setStatus(SimpleTransactionConstant.STATUS_CANCEL_FAILED);
            }
        } else {
            transaction.setStatus(SimpleTransactionConstant.STATUS_SUCCESS);
        }
        return !hasFailedTransaction;
    }

    @Override
    public boolean cancelSubTransaction(SagaSimpleTransaction transaction, SagaSimpleSubTransaction subTransaction) {
        if (SimpleTransactionUtils.isTransactionCancelEnded(subTransaction)) {
            return true;
        }
        SimpleSagaMeta simpleSagaMeta = SimpleTransactionMetaHelper.getSageMeta(subTransaction.getTxKey(), subTransaction.getMethodKey());
        boolean result = false;
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
                result = true;
                subTransaction.setStatus(SimpleTransactionConstant.STATUS_SUCCESS);
            } catch (NoSuchMethodException | ClassNotFoundException | IllegalAccessException | InvocationTargetException e) {
                log.error("执行Cancel错误", e);
                if (transaction.getRetryTimes() < getRetryTimes()) {
                    subTransaction.setStatus(SimpleTransactionConstant.STATUS_CANCEL_FAILED0);
                } else {
                    subTransaction.setStatus(SimpleTransactionConstant.STATUS_CANCEL_FAILED);
                }
                subTransaction.setMessage(e.getMessage());
            }
        }
        return result;
    }

}

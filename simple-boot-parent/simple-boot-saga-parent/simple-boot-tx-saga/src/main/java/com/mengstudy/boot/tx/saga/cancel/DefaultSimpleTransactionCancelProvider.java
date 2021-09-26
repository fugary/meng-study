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
        log.info("Canceling transaction [{}/{}]", transaction.getTxId(), transaction.getTxKey());
        transaction.setRetryTimes(transaction.getRetryTimes() + 1);
        List<SagaSimpleSubTransaction> subTransactions = new ArrayList<>(sagaSubTransactions);
        subTransactions.sort(Comparator.comparing(SagaSimpleSubTransaction::getIdxNo).reversed());
        boolean hasFailedTransaction = false;
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
        log.info("Canceling sub transaction [{}/{}/{}]", subTransaction.getTxId(), subTransaction.getSubTxId(), subTransaction.getTxKey());
        SimpleSagaMeta simpleSagaMeta = SimpleTransactionMetaHelper.getSageMeta(subTransaction.getTxKey());
        boolean result = false;
        if (simpleSagaMeta != null) {
            Object targetBean = simpleSagaMeta.getTargetBean();
            try {
                List<String> paramTypeStrs = fromJson(subTransaction.getParamClazz(), List.class);
                List<String> paramDataList = fromJson(subTransaction.getParamData(), List.class);
                List<Class<?>> paramTypes = new ArrayList<>();
                List<Object> params = new ArrayList<>();
                for (int i = 0; i < paramDataList.size(); i++) {
                    Class<?> paramClass = Class.forName(paramTypeStrs.get(i));
                    paramTypes.add(paramClass);
                    params.add(fromJson(paramDataList.get(i), paramClass));
                }
                Method method = Class.forName(simpleSagaMeta.getServiceClass()).getMethod(subTransaction.getCancelMethod(),
                        paramTypes.toArray(new Class[0]));
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

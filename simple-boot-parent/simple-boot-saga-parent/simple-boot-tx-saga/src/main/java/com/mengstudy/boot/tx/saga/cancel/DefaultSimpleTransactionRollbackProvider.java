package com.mengstudy.boot.tx.saga.cancel;

import com.mengstudy.boot.tx.saga.dto.SagaSimpleSubTransaction;
import com.mengstudy.boot.tx.saga.dto.SagaSimpleTransaction;
import com.mengstudy.boot.tx.saga.meta.SimpleSagaMeta;
import com.mengstudy.boot.tx.saga.meta.SimpleTransactionMetaHelper;
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
public class DefaultSimpleTransactionRollbackProvider implements SimpleTransactionRollbackProvider {

    @Override
    public void rollbackSimpleTransaction(SagaSimpleTransaction transaction, List<SagaSimpleSubTransaction> sagaSubTransactions) {
        List<SagaSimpleSubTransaction> subTransactions = new ArrayList<>(sagaSubTransactions);
        subTransactions.sort(Comparator.comparing(SagaSimpleSubTransaction::getIdxNo).reversed());
        for (SagaSimpleSubTransaction subTransaction : subTransactions) {
            this.rollbackSubTransaction(subTransaction);
        }
    }

    @Override
    public void rollbackSubTransaction(SagaSimpleSubTransaction subTransaction) {
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
                log.error("查找方法错误", e);
            }
        }
    }

}

package com.mengstudy.boot.tx.saga.interceptor;

import com.mengstudy.boot.tx.saga.annotation.SimpleSaga;
import com.mengstudy.boot.tx.saga.dto.SagaRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.UUID;

/**
 * Created on 2021/9/23 16:26 .<br>
 *
 * @author gary.fu
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SimpleTransactionUtils {

    private static final ThreadLocal<SimpleTransactionContext> SIMPLE_TRANSACTION_CONTEXT_KEY = new ThreadLocal<>();

    private static final ThreadLocal<SubTransaction> SUB_TRANSACTION_KEY = new ThreadLocal<>();

    public static final Integer STATUS_PENDING = 0;

    public static final Integer STATUS_SUCCESS = 1;

    public static final Integer STATUS_FAILED = 2;

    public static final Integer STATUS_TIMEOUT = 3;

    public static SimpleTransactionContext getCurrentContext() {
        return SIMPLE_TRANSACTION_CONTEXT_KEY.get();
    }

    public static void startSimpleTransaction(SimpleTransactionContext context) {
        SIMPLE_TRANSACTION_CONTEXT_KEY.set(context);
    }

    public static void endSimpleTransaction() {
        SIMPLE_TRANSACTION_CONTEXT_KEY.remove();
    }


    public static SubTransaction getSubTransaction() {
        return SUB_TRANSACTION_KEY.get();
    }

    public static void startSubTransaction(SubTransaction subTransaction) {
        SUB_TRANSACTION_KEY.set(subTransaction);
    }

    public static void endSubTransaction() {
        SUB_TRANSACTION_KEY.remove();
    }

    /**
     * 获取UUID
     *
     * @return
     */
    public static String getUuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 创建
     *
     * @return
     */
    public static SimpleTransactionContext create() {
        SimpleTransactionContext simpleTransactionContext = new SimpleTransactionContext();
        simpleTransactionContext.setStartDate(new Date());
        simpleTransactionContext.setTxId(getUuid());
        simpleTransactionContext.setStatus(STATUS_PENDING);
        return simpleTransactionContext;
    }

    /**
     * create sub transaction
     *
     * @param method
     * @return
     */
    public static SubTransaction createSub(Method method, Object[] args) {
        SimpleTransactionContext currentContext = getCurrentContext();
        SubTransaction subTransaction = new SubTransaction();
        subTransaction.setStartDate(new Date());
        subTransaction.setTxId(currentContext.getTxId());
        subTransaction.setSubTxId(getUuid());
        subTransaction.setStatus(STATUS_PENDING);
        subTransaction.setIdxNo(currentContext.getSubIndex().getAndIncrement());
        SimpleSaga simpleSaga = AnnotationUtils.findAnnotation(method, SimpleSaga.class);
        if (simpleSaga != null) {
            String cancelMethod = simpleSaga.cancelMethod();
            subTransaction.setCancelMethod(cancelMethod);
        }
        subTransaction.setParamClazz(method.getParameterTypes()[0].getName());
        return subTransaction;
    }

    /**
     * 计算Request
     *
     * @param context
     * @return
     */
    public static SagaRequest toSagaRequest(SimpleTransactionContext context) {
        SagaRequest request = new SagaRequest();
        request.setTxId(context.getTxId());
        request.setStartDate(context.getStartDate());
        request.setEndDate(context.getEndDate());
        request.setStatus(context.getStatus());
        request.setMessage(context.getMessage());
        if (context instanceof SubTransaction) {
            SubTransaction subTransaction = (SubTransaction) context;
            request.setCancelMethod(subTransaction.getCancelMethod());
            request.setParamClazz(subTransaction.getParamClazz());
            request.setParamData(subTransaction.getParamData());
            request.setSubTxId(subTransaction.getSubTxId());
            request.setServiceName(subTransaction.getServiceName());
            request.setServiceClazz(subTransaction.getServiceClazz());
        }
        return request;
    }
}

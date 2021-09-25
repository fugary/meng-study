package com.mengstudy.boot.tx.saga.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mengstudy.boot.tx.saga.annotation.SimpleSaga;
import com.mengstudy.boot.tx.saga.annotation.SimpleTransactional;
import com.mengstudy.boot.tx.saga.dto.SagaRequest;
import com.mengstudy.boot.tx.saga.meta.SimpleSagaMeta;
import com.mengstudy.boot.tx.saga.meta.SimpleTransactionMetaHelper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;

import java.io.IOException;
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

    private static final Logger logger = LoggerFactory.getLogger(SimpleTransactionUtils.class);

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

    public static <T> String getTxKey(Class<T> clazz, Method method) {
        return clazz.getName() + "#" + method.getName();
    }

    public static <T> String getMethodKey(Method method) {
        StringBuilder sb = new StringBuilder(method.getName());
        for (Class<?> parameterType : method.getParameterTypes()) {
            sb.append("$").append(parameterType.getName());
        }
        return sb.toString();
    }

    /**
     * 创建
     *
     * @return
     */
    public static SimpleTransactionContext create(SimpleTransactional simpleTransactional, String txKey) {
        SimpleTransactionContext simpleTransactionContext = new SimpleTransactionContext();
        simpleTransactionContext.setStartDate(new Date());
        simpleTransactionContext.setTxId(getUuid());
        simpleTransactionContext.setTxKey(txKey);
        simpleTransactionContext.setStatus(STATUS_PENDING);
        simpleTransactionContext.setTxName(simpleTransactional.name());
        return simpleTransactionContext;
    }

    /**
     * create sub transaction
     *
     * @param method
     * @return
     */
    public static <T> SubTransaction createSub(Class<T> targetClass, Method method, Object[] args) {
        SimpleTransactionContext currentContext = getCurrentContext();
        SubTransaction subTransaction = new SubTransaction();
        subTransaction.setStartDate(new Date());
        subTransaction.setTxId(currentContext.getTxId());
        subTransaction.setSubTxId(getUuid());
        subTransaction.setStatus(STATUS_PENDING);
        subTransaction.setIdxNo(currentContext.getSubIndex().getAndIncrement());
        String txKey = getTxKey(targetClass, method);
        subTransaction.setTxKey(txKey);
        String methodKey = getMethodKey(method);
        SimpleSagaMeta sageMeta = SimpleTransactionMetaHelper.getSageMeta(txKey, methodKey);
        subTransaction.setServiceName(sageMeta.getServiceName());
        subTransaction.setServiceClazz(sageMeta.getServiceClass());
        subTransaction.setCancelMethod(sageMeta.getCancelMethod());
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
        request.setTxKey(context.getTxKey());
        request.setTxName(context.getTxName());
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

    /**
     * 转换成Json
     *
     * @param mapper
     * @param target
     * @return
     */
    public static String toJson(ObjectMapper mapper, Object target) {
        String result = "";
        try {
            result = mapper.writeValueAsString(target);
        } catch (IOException e) {
            logger.error("将对象转换成Json出错", e);
        }
        return result;
    }

    /**
     * 解析Json成对象
     *
     * @param mapper
     * @param json
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T fromJson(ObjectMapper mapper, String json, Class<T> clazz) {
        T result = null;
        try {
            result = mapper.readValue(json, clazz);
        } catch (IOException e) {
            logger.error("将Json转成对象报错", e);
        }
        return result;
    }
}

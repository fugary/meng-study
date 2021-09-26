package com.mengstudy.boot.tx.saga.interceptor;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mengstudy.boot.tx.saga.annotation.SimpleTransactional;
import com.mengstudy.boot.tx.saga.constant.SimpleTransactionConstant;
import com.mengstudy.boot.tx.saga.dto.BaseSimpleTransaction;
import com.mengstudy.boot.tx.saga.dto.SagaSimpleSubTransaction;
import com.mengstudy.boot.tx.saga.dto.SagaSimpleTransaction;
import com.mengstudy.boot.tx.saga.meta.SimpleSagaMeta;
import com.mengstudy.boot.tx.saga.meta.SimpleTransactionMetaHelper;
import com.mengstudy.boot.tx.saga.provider.rest.SagaRequest;
import com.mengstudy.boot.tx.saga.provider.rest.SagaSubRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.mengstudy.boot.tx.saga.constant.SimpleTransactionConstant.STATUS_PENDING;

/**
 * Created on 2021/9/23 16:26 .<br>
 *
 * @author gary.fu
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class SimpleTransactionUtils {

    private static final ThreadLocal<SimpleTransactionContext> SIMPLE_TRANSACTION_CONTEXT_KEY = new ThreadLocal<>();

    private static final ThreadLocal<SubTransactionContext> SUB_TRANSACTION_KEY = new ThreadLocal<>();

    private static final ObjectMapper objectMapper;

    static {
        objectMapper = Jackson2ObjectMapperBuilder.json()
                .serializationInclusion(JsonInclude.Include.NON_NULL)
                .failOnUnknownProperties(false)
                .build();
    }

    public static SimpleTransactionContext getCurrentContext() {
        return SIMPLE_TRANSACTION_CONTEXT_KEY.get();
    }

    public static void startSimpleTransaction(SimpleTransactionContext context) {
        SIMPLE_TRANSACTION_CONTEXT_KEY.set(context);
    }

    public static void endSimpleTransaction() {
        SIMPLE_TRANSACTION_CONTEXT_KEY.remove();
    }


    public static SubTransactionContext getSubTransaction() {
        return SUB_TRANSACTION_KEY.get();
    }

    public static void startSubTransaction(SubTransactionContext subTransactionContext) {
        SUB_TRANSACTION_KEY.set(subTransactionContext);
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

    public static String getMethodKey(Method method) {
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
        SagaSimpleTransaction transaction = new SagaSimpleTransaction();
        transaction.setStartDate(new Date());
        transaction.setTxId(getUuid());
        transaction.setTxKey(txKey);
        transaction.setStatus(STATUS_PENDING);
        transaction.setTxName(simpleTransactional.name());
        simpleTransactionContext.setTransaction(transaction);
        return simpleTransactionContext;
    }

    /**
     * create sub transaction
     *
     * @param method
     * @return
     */
    public static <T> SubTransactionContext createSub(Class<T> targetClass, Method method, Object[] args) {
        SimpleTransactionContext currentContext = getCurrentContext();
        SagaSimpleTransaction transaction = currentContext.getTransaction();
        SagaSimpleSubTransaction sagaSubTransaction = new SagaSimpleSubTransaction();
        sagaSubTransaction.setStartDate(new Date());
        sagaSubTransaction.setTxId(transaction.getTxId());
        sagaSubTransaction.setSubTxId(getUuid());
        sagaSubTransaction.setStatus(STATUS_PENDING);
        sagaSubTransaction.setIdxNo(currentContext.getSubIndex().getAndIncrement());
        String txKey = getTxKey(targetClass, method);
        sagaSubTransaction.setTxKey(txKey);
        String methodKey = getMethodKey(method);
        SimpleSagaMeta sageMeta = SimpleTransactionMetaHelper.getSageMeta(txKey, methodKey);
        sagaSubTransaction.setServiceName(sageMeta.getServiceName());
        sagaSubTransaction.setServiceClazz(sageMeta.getServiceClass());
        sagaSubTransaction.setMethodKey(sageMeta.getMethodKey());
        sagaSubTransaction.setCancelMethod(sageMeta.getCancelMethod());
        List<String> typeList = Arrays.stream(method.getParameterTypes()).map(Class::getName).collect(Collectors.toList());
        sagaSubTransaction.setParamClazz(toJson(typeList));
        List<String> dataList = Arrays.stream(args).map(SimpleTransactionUtils::toJson).collect(Collectors.toList());
        sagaSubTransaction.setParamData(toJson(dataList));
        SubTransactionContext subTransactionContext = new SubTransactionContext();
        subTransactionContext.setTransaction(transaction);
        subTransactionContext.setSagaTransaction(sagaSubTransaction);
        return subTransactionContext;
    }

    /**
     * 计算Request
     *
     * @param context
     * @return
     */
    public static SagaRequest toSagaRequest(SimpleTransactionContext context) {
        SagaSimpleTransaction transaction = context.getTransaction();
        SagaRequest request = new SagaRequest();
        request.setTransaction(transaction);
        return request;
    }

    /**
     * 计算Request
     *
     * @param context
     * @return
     */
    public static SagaSubRequest toSagaSubRequest(SubTransactionContext context) {
        SagaSubRequest request = new SagaSubRequest();
        request.setTransaction(context.getTransaction());
        request.setSubTransaction(context.getSagaTransaction());
        return request;
    }

    /**
     * 转换成Json
     *
     * @param target
     * @return
     */
    public static String toJson(Object target) {
        String result = "";
        try {
            result = objectMapper.writeValueAsString(target);
        } catch (IOException e) {
            log.error("将对象转换成Json出错", e);
        }
        return result;
    }

    /**
     * 解析Json成对象
     *
     * @param json
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        T result = null;
        try {
            result = objectMapper.readValue(json, clazz);
        } catch (IOException e) {
            log.error("将Json转成对象报错", e);
        }
        return result;
    }

    /**
     * 事务终态
     *
     * @param transaction
     * @return
     */
    public static boolean isTransactionEnded(BaseSimpleTransaction transaction){
        return SimpleTransactionConstant.STATUS_SUCCESS.equals(transaction.getStatus())
                || SimpleTransactionConstant.STATUS_CANCEL_FAILED.equals(transaction.getStatus())
                || SimpleTransactionConstant.STATUS_CANCELED.equals(transaction.getStatus());
    }

    /**
     * 取消事务终态
     *
     * @param transaction
     * @return
     */
    public static boolean isTransactionCancelEnded(BaseSimpleTransaction transaction){
        return SimpleTransactionConstant.STATUS_CANCEL_FAILED.equals(transaction.getStatus())
                || SimpleTransactionConstant.STATUS_CANCELED.equals(transaction.getStatus());
    }

    /**
     * 需要取消事务
     *
     * @param transaction
     * @return
     */
    public static boolean isTransactionNeedCancel(BaseSimpleTransaction transaction){
        return SimpleTransactionConstant.STATUS_FAILED.equals(transaction.getStatus())
                || SimpleTransactionConstant.STATUS_CANCEL_FAILED0.equals(transaction.getStatus());
    }
}

package com.mengstudy.boot.tx.saga.interceptor;

import com.mengstudy.boot.tx.saga.annotation.SimpleTransactional;
import com.mengstudy.boot.tx.saga.dto.SagaSimpleTransaction;
import com.mengstudy.boot.tx.saga.provider.SimpleTransactionProvider;
import lombok.Getter;
import lombok.Setter;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;
import java.util.Date;

/**
 * Created on 2021/9/23 16:05 .<br>
 *
 * @author gary.fu
 */
@Aspect
public class SimpleTransactionalInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(SimpleTransactionalInterceptor.class);

    @Getter
    @Setter
    private SimpleTransactionProvider simpleTransactionProvider;

    @Around("@annotation(com.mengstudy.boot.tx.saga.annotation.SimpleTransactional)")
    public Object intercept(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        SimpleTransactional simpleTransactional = AnnotationUtils.findAnnotation(method, SimpleTransactional.class);
        Class<?> targetClass = AopUtils.getTargetClass(joinPoint.getTarget());
        String txKey = SimpleTransactionUtils.getTxKey(targetClass, method);
        SimpleTransactionContext transactionContext = SimpleTransactionUtils.create(simpleTransactional, txKey);
        SimpleTransactionUtils.startSimpleTransaction(transactionContext);
        Object result = null;
        try {
            startTransaction(transactionContext);
            result = joinPoint.proceed();
            endTransaction(transactionContext, SimpleTransactionUtils.STATUS_SUCCESS);
        } catch (Exception e) {
            logger.error("执行事务报错", e);
            endTransaction(transactionContext, SimpleTransactionUtils.STATUS_FAILED);
            throw e;
        } finally {
            SimpleTransactionUtils.endSimpleTransaction();
        }
        return result;
    }

    /**
     * 开始事务
     *
     * @param context
     */
    protected void startTransaction(SimpleTransactionContext context) {
        if (simpleTransactionProvider != null) {
            try {
                simpleTransactionProvider.startSimpleTransaction(context);
            } catch (Exception e) {
                logger.error("[框架]:开始事务报错", e);
            }
        }
    }

    /**
     * 事务结束
     *
     * @param context
     * @param status
     */
    protected void endTransaction(SimpleTransactionContext context, Integer status) {
        SagaSimpleTransaction transaction = context.getTransaction();
        transaction.setStatus(status);
        transaction.setEndDate(new Date());
        if (simpleTransactionProvider != null) {
            try {
                simpleTransactionProvider.endSimpleTransaction(context);
            } catch (Exception e) {
                logger.error("[框架]:结束事务报错", e);
            }
        }
    }

}

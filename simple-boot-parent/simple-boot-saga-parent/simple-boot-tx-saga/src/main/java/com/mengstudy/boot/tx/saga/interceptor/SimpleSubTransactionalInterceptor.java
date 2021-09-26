package com.mengstudy.boot.tx.saga.interceptor;

import com.mengstudy.boot.tx.saga.dto.SagaSimpleSubTransaction;
import com.mengstudy.boot.tx.saga.provider.SimpleTransactionProvider;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.aop.support.AopUtils;

import java.lang.reflect.Method;
import java.util.Date;

/**
 * Created on 2021/9/23 16:05 .<br>
 *
 * @author gary.fu
 */
@Aspect
@Slf4j
public class SimpleSubTransactionalInterceptor {

    @Getter
    @Setter
    private SimpleTransactionProvider simpleTransactionProvider;

    @Around("@annotation(com.mengstudy.boot.tx.saga.annotation.SimpleSaga)")
    public Object intercept(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Object[] args = joinPoint.getArgs();
        Class<?> targetClass = AopUtils.getTargetClass(joinPoint.getTarget());
        SubTransactionContext subTransactionContext = SimpleTransactionUtils.createSub(targetClass, method, args);
        Object result = null;
        try {
            SimpleTransactionUtils.startSubTransaction(subTransactionContext);
            result = joinPoint.proceed();
            endSubTransaction(subTransactionContext, SimpleTransactionUtils.STATUS_SUCCESS);
        } catch (Exception e) {
            log.error("执行事务报错", e);
            endSubTransaction(subTransactionContext, SimpleTransactionUtils.STATUS_FAILED);
            throw e;
        } finally {
            SimpleTransactionUtils.endSubTransaction();
        }
        return result;
    }

    /**
     * 事务结束
     *
     * @param subTransactionContext
     * @param status
     */
    protected void endSubTransaction(SubTransactionContext subTransactionContext, Integer status) {
        SagaSimpleSubTransaction sagaTransaction = subTransactionContext.getSagaTransaction();
        sagaTransaction.setStatus(status);
        sagaTransaction.setEndDate(new Date());
        if (simpleTransactionProvider != null) {
            try {
                simpleTransactionProvider.recordSubTransaction(subTransactionContext);
            } catch (Exception e) {
                log.error("[框架]:结束事务报错", e);
            }
        }
    }

}

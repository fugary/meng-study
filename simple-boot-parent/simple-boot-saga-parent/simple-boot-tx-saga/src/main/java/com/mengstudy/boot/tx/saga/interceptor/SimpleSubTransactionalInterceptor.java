package com.mengstudy.boot.tx.saga.interceptor;

import com.mengstudy.boot.tx.saga.annotation.SimpleSaga;
import com.mengstudy.boot.tx.saga.provider.SimpleTransactionProvider;
import lombok.Getter;
import lombok.Setter;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;
import java.util.Date;

/**
 * Created on 2021/9/23 16:05 .<br>
 *
 * @author gary.fu
 */
@Aspect
public class SimpleSubTransactionalInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(SimpleSubTransactionalInterceptor.class);

    @Getter
    @Setter
    private SimpleTransactionProvider simpleTransactionProvider;

    @Around("@annotation(SimpleSaga)")
    public Object intercept(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Object[] args = joinPoint.getArgs();
        SubTransaction subTransaction = SimpleTransactionUtils.createSub(method, args);
        Object result = null;
        try {
            SimpleTransactionUtils.startSubTransaction(subTransaction);
            startTransaction(subTransaction);
            result = joinPoint.proceed();
            endTransaction(subTransaction, SimpleTransactionUtils.STATUS_SUCCESS);
        } catch (Exception e) {
            logger.error("执行事务报错", e);
            endTransaction(subTransaction, SimpleTransactionUtils.STATUS_FAILED);
            throw e;
        } finally {
            SimpleTransactionUtils.endSubTransaction();
        }
        return result;
    }

    /**
     * 开始事务
     *
     * @param subTransaction
     */
    protected void startTransaction(SubTransaction subTransaction) {
        if (simpleTransactionProvider != null) {
            try {
                simpleTransactionProvider.startSubTransaction(subTransaction);
            } catch (Exception e) {
                logger.error("[框架]:开始事务报错", e);
            }
        }
    }

    /**
     * 事务结束
     *
     * @param subTransaction
     * @param status
     */
    protected void endTransaction(SubTransaction subTransaction, Integer status) {
        subTransaction.setStatus(status);
        subTransaction.setEndDate(new Date());
        if (simpleTransactionProvider != null) {
            try {
                simpleTransactionProvider.endSubTransaction(subTransaction);
            } catch (Exception e) {
                logger.error("[框架]:结束事务报错", e);
            }
        }
    }

}

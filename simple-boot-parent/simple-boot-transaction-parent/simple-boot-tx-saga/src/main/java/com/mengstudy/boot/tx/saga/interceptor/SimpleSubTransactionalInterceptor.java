package com.mengstudy.boot.tx.saga.interceptor;

import com.mengstudy.boot.tx.saga.provider.SimpleTransactionProvider;
import lombok.Getter;
import lombok.Setter;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        SimpleTransactionContext transactionContext = SimpleTransactionUtils.getCurrentContext();
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
                simpleTransactionProvider.startSubTransaction();
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
        context.setStatus(status);
        context.setEndDate(new Date());
        if (simpleTransactionProvider != null) {
            try {
                simpleTransactionProvider.endSubTransaction();
            } catch (Exception e) {
                logger.error("[框架]:结束事务报错", e);
            }
        }
    }

}

package com.mengstudy.boot.tx.saga.interceptor;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

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
}

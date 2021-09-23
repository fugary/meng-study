package com.mengstudy.boot.tx.saga.provider;

import com.mengstudy.boot.tx.saga.interceptor.SimpleTransactionContext;
import com.mengstudy.boot.tx.saga.interceptor.SubTransaction;

/**
 * Created on 2021/9/23 16:54 .<br>
 *
 * @author gary.fu
 */
public interface SimpleTransactionProvider {

    void startSimpleTransaction(SimpleTransactionContext context);

    void endSimpleTransaction(SimpleTransactionContext context);

    void startSubTransaction(SubTransaction transaction);

    void endSubTransaction(SubTransaction transaction);
}

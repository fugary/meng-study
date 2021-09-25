package com.mengstudy.boot.tx.saga.cancel;

import com.mengstudy.boot.tx.saga.dto.SagaSimpleTransaction;
import com.mengstudy.boot.tx.saga.provider.SimpleTransactionProvider;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created on 2021/9/24 11:40 .<br>
 *
 * @author gary.fu
 */
@AllArgsConstructor
public class SimpleTransactionCancelTask implements Runnable {

    @Getter
    @Setter
    private SimpleTransactionProvider simpleTransactionProvider;

    @Getter
    @Setter
    private Set<String> transactionKeys;

    @Override
    public void run() {
        List<SagaSimpleTransaction> transactions = simpleTransactionProvider.loadFailed(new ArrayList<>(transactionKeys));
        for (SagaSimpleTransaction transaction : transactions) {
            // 回滚事务
            simpleTransactionProvider.cancelSimpleTransaction(transaction);
        }
    }
}

package com.mengstudy.boot.tx.saga.cancel;

import com.mengstudy.boot.tx.saga.dto.SagaSimpleTransaction;
import com.mengstudy.boot.tx.saga.provider.SimpleTransactionProvider;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created on 2021/9/24 11:40 .<br>
 *
 * @author gary.fu
 */
@AllArgsConstructor
@Getter
@Setter
@Slf4j
public class SimpleTransactionCancelTask implements Runnable {

    private SimpleTransactionProvider simpleTransactionProvider;

    private Set<String> transactionKeys;

    @Override
    public void run() {
        List<SagaSimpleTransaction> transactions = simpleTransactionProvider.loadFailed(new ArrayList<>(transactionKeys));
        for (SagaSimpleTransaction transaction : transactions) {
            // 回滚事务
            try {
                simpleTransactionProvider.cancelSimpleTransaction(transaction);
            } catch (Exception e) {
                log.error("执行cancel任务失败", e);
            }
        }
    }
}

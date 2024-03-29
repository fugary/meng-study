package com.mengstudy.boot.tx.saga.provider.jdbc;

import com.mengstudy.boot.tx.saga.dto.SagaSimpleTransaction;
import com.mengstudy.boot.tx.saga.interceptor.SimpleTransactionContext;
import com.mengstudy.boot.tx.saga.interceptor.SubTransactionContext;
import com.mengstudy.boot.tx.saga.provider.AbstractSimpleTransactionProvider;
import lombok.Getter;
import lombok.Setter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Gary Fu
 * @date 2021/9/25 12:21
 */
public class JdbcSimpleTransactionProviderImpl extends AbstractSimpleTransactionProvider {

    @Getter
    @Setter
    private JdbcTemplate jdbcTemplate;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void startSimpleTransaction(SimpleTransactionContext context) {

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void endSimpleTransaction(SimpleTransactionContext context) {

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void recordSubTransaction(SubTransactionContext transaction) {

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<SagaSimpleTransaction> loadFailed(List<String> keys) {
        return null;
    }

    @Override
    public void cancelSimpleTransaction(SagaSimpleTransaction simpleTransaction) {

    }

    @Override
    public void updateSimpleTransaction(SagaSimpleTransaction simpleTransaction) {

    }
}

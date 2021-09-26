package com.mengstudy.boot.tx.saga.demo.config;

import com.mengstudy.boot.tx.saga.cancel.DefaultSimpleTransactionRollbackProvider;
import com.mengstudy.boot.tx.saga.cancel.SimpleTransactionRollbackProvider;
import com.mengstudy.boot.tx.saga.cancel.SimpleTransactionScanner;
import com.mengstudy.boot.tx.saga.interceptor.SimpleSubTransactionalInterceptor;
import com.mengstudy.boot.tx.saga.interceptor.SimpleTransactionalInterceptor;
import com.mengstudy.boot.tx.saga.provider.SimpleTransactionProvider;
import com.mengstudy.boot.tx.saga.provider.memory.InMemorySimpleTransactionProviderImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Gary Fu
 * @date 2021/9/25 14:51
 */
@Configuration
public class SimpleTransactionConfiguration {

    @Bean
    public SimpleTransactionRollbackProvider simpleTransactionRollbackProvider() {
        return new DefaultSimpleTransactionRollbackProvider();
    }

    @Bean
    public SimpleTransactionProvider simpleTransactionProvider() {
        InMemorySimpleTransactionProviderImpl simpleTransactionProvider = new InMemorySimpleTransactionProviderImpl();
        simpleTransactionProvider.setSimpleTransactionRollbackProvider(simpleTransactionRollbackProvider());
        return simpleTransactionProvider;
    }

    @Bean
    public SimpleTransactionalInterceptor simpleTransactionalInterceptor() {
        SimpleTransactionalInterceptor simpleTransactionalInterceptor = new SimpleTransactionalInterceptor();
        simpleTransactionalInterceptor.setSimpleTransactionProvider(simpleTransactionProvider());
        return simpleTransactionalInterceptor;
    }

    @Bean
    public SimpleSubTransactionalInterceptor simpleSubTransactionalInterceptor() {
        SimpleSubTransactionalInterceptor simpleSubTransactionalInterceptor = new SimpleSubTransactionalInterceptor();
        simpleSubTransactionalInterceptor.setSimpleTransactionProvider(simpleTransactionProvider());
        return simpleSubTransactionalInterceptor;
    }

    @Bean
    public SimpleTransactionScanner simpleTransactionScanner() {
        SimpleTransactionScanner simpleTransactionScanner = new SimpleTransactionScanner();
        simpleTransactionScanner.setSimpleTransactionProvider(simpleTransactionProvider());
        return simpleTransactionScanner;
    }
}

package com.mengstudy.boot.tx.saga.starter;

import com.mengstudy.boot.tx.saga.cancel.DefaultSimpleTransactionCancelProvider;
import com.mengstudy.boot.tx.saga.cancel.SimpleTransactionCancelProvider;
import com.mengstudy.boot.tx.saga.cancel.SimpleTransactionScanner;
import com.mengstudy.boot.tx.saga.interceptor.SimpleSubTransactionalInterceptor;
import com.mengstudy.boot.tx.saga.interceptor.SimpleTransactionalInterceptor;
import com.mengstudy.boot.tx.saga.provider.SimpleTransactionProvider;
import com.mengstudy.boot.tx.saga.provider.jdbc.JdbcSimpleTransactionProviderImpl;
import com.mengstudy.boot.tx.saga.provider.memory.InMemorySimpleTransactionProviderImpl;
import com.mengstudy.boot.tx.saga.provider.rest.RestSimpleTransactionProviderImpl;
import com.mengstudy.boot.tx.saga.starter.config.SimpleTransactionConfigProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Gary Fu
 * @date 2021/9/25 14:51
 */
@Configuration
@ConditionalOnProperty(value = "simple.transaction.saga.enabled", matchIfMissing = true)
@EnableConfigurationProperties(SimpleTransactionConfigProperties.class)
public class SimpleTransactionAutoConfiguration {

    @Getter
    @Setter(onMethod_ = @Autowired)
    private SimpleTransactionConfigProperties simpleTransactionConfigProperties;

    @Bean
    public SimpleTransactionCancelProvider simpleTransactionCancelProvider() {
        return new DefaultSimpleTransactionCancelProvider();
    }

    @Bean
    @ConditionalOnProperty(value = "simple.transaction.saga.provider", havingValue = "IN_MEMORY", matchIfMissing = true)
    public SimpleTransactionProvider inMemorySimpleTransactionProvider() {
        InMemorySimpleTransactionProviderImpl simpleTransactionProvider = new InMemorySimpleTransactionProviderImpl();
        simpleTransactionProvider.setSimpleTransactionCancelProvider(simpleTransactionCancelProvider());
        return simpleTransactionProvider;
    }

    @Bean
    @ConditionalOnProperty(value = "simple.transaction.saga.provider", havingValue = "JDBC", matchIfMissing = false)
    public SimpleTransactionProvider jdbcSimpleTransactionProvider() {
        JdbcSimpleTransactionProviderImpl simpleTransactionProvider = new JdbcSimpleTransactionProviderImpl();
        simpleTransactionProvider.setSimpleTransactionCancelProvider(simpleTransactionCancelProvider());
        simpleTransactionProvider.setPageSize(simpleTransactionConfigProperties.getSaga().getPageSize());
        return simpleTransactionProvider;
    }

    @Bean
    @ConditionalOnProperty(value = "simple.transaction.saga.provider", havingValue = "REST", matchIfMissing = false)
    public SimpleTransactionProvider restSimpleTransactionProvider() {
        RestSimpleTransactionProviderImpl simpleTransactionProvider = new RestSimpleTransactionProviderImpl();
        simpleTransactionProvider.setSimpleTransactionCancelProvider(simpleTransactionCancelProvider());
        simpleTransactionProvider.setPageSize(simpleTransactionConfigProperties.getSaga().getPageSize());
        return simpleTransactionProvider;
    }

    @Bean
    public SimpleTransactionalInterceptor simpleTransactionalInterceptor() {
        SimpleTransactionalInterceptor simpleTransactionalInterceptor = new SimpleTransactionalInterceptor();
        simpleTransactionalInterceptor.setSimpleTransactionProvider(inMemorySimpleTransactionProvider());
        return simpleTransactionalInterceptor;
    }

    @Bean
    public SimpleSubTransactionalInterceptor simpleSubTransactionalInterceptor() {
        SimpleSubTransactionalInterceptor simpleSubTransactionalInterceptor = new SimpleSubTransactionalInterceptor();
        simpleSubTransactionalInterceptor.setSimpleTransactionProvider(inMemorySimpleTransactionProvider());
        return simpleSubTransactionalInterceptor;
    }

    @Bean
    public SimpleTransactionScanner simpleTransactionScanner() {
        SimpleTransactionScanner simpleTransactionScanner = new SimpleTransactionScanner();
        simpleTransactionScanner.setSimpleTransactionProvider(inMemorySimpleTransactionProvider());
        simpleTransactionScanner.setDelayTime(simpleTransactionConfigProperties.getSaga().getDelayTime());
        simpleTransactionScanner.setPeriod(simpleTransactionConfigProperties.getSaga().getPeriod());
        return simpleTransactionScanner;
    }
}

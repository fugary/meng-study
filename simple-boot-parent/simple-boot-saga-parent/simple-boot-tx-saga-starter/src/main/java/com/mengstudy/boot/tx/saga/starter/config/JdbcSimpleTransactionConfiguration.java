package com.mengstudy.boot.tx.saga.starter.config;

import com.mengstudy.boot.tx.saga.cancel.SimpleTransactionCancelProvider;
import com.mengstudy.boot.tx.saga.provider.SimpleTransactionProvider;
import com.mengstudy.boot.tx.saga.provider.jdbc.JdbcSimpleTransactionProviderImpl;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Created on 2021/9/26 17:25 .<br>
 *
 * @author gary.fu
 */
@Configuration
@ConditionalOnBean(JdbcTemplate.class)
public class JdbcSimpleTransactionConfiguration {

    @Getter
    @Setter(onMethod_ = @Autowired)
    private SimpleTransactionCancelProvider simpleTransactionCancelProvider;

    @Getter
    @Setter(onMethod_ = @Autowired)
    private SimpleTransactionConfigProperties simpleTransactionConfigProperties;

    @Getter
    @Setter(onMethod_ = @Autowired)
    private JdbcTemplate jdbcTemplate;

    @Bean
    @ConditionalOnProperty(value = "simple.transaction.saga.provider", havingValue = "JDBC")
    public SimpleTransactionProvider jdbcSimpleTransactionProvider() {
        JdbcSimpleTransactionProviderImpl simpleTransactionProvider = new JdbcSimpleTransactionProviderImpl();
        simpleTransactionProvider.setSimpleTransactionCancelProvider(simpleTransactionCancelProvider);
        simpleTransactionProvider.setPageSize(simpleTransactionConfigProperties.getSaga().getPageSize());
        simpleTransactionProvider.setJdbcTemplate(jdbcTemplate);
        return simpleTransactionProvider;
    }
}

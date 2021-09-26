package com.mengstudy.boot.tx.saga.starter.config;

import com.mengstudy.boot.tx.saga.cancel.SimpleTransactionCancelProvider;
import com.mengstudy.boot.tx.saga.provider.SimpleTransactionProvider;
import com.mengstudy.boot.tx.saga.provider.rest.RestSimpleTransactionProviderImpl;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Created on 2021/9/26 17:25 .<br>
 *
 * @author gary.fu
 */
@Configuration
@ConditionalOnBean(RestTemplate.class)
public class RestSimpleTransactionConfiguration {

    @Getter
    @Setter(onMethod_ = @Autowired)
    private SimpleTransactionCancelProvider simpleTransactionCancelProvider;

    @Getter
    @Setter(onMethod_ = @Autowired)
    private SimpleTransactionConfigProperties simpleTransactionConfigProperties;

    @Getter
    @Setter(onMethod_ = @Autowired)
    private RestTemplate restTemplate;

    @Bean
    @ConditionalOnProperty(value = "simple.transaction.saga.provider", havingValue = "REST")
    public SimpleTransactionProvider restSimpleTransactionProvider() {
        RestSimpleTransactionProviderImpl simpleTransactionProvider = new RestSimpleTransactionProviderImpl();
        simpleTransactionProvider.setSimpleTransactionCancelProvider(simpleTransactionCancelProvider);
        simpleTransactionProvider.setPageSize(simpleTransactionConfigProperties.getSaga().getPageSize());
        simpleTransactionProvider.setRestTemplate(restTemplate);
        return simpleTransactionProvider;
    }
}

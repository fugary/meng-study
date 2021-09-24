package com.mengstudy.boot.tx.saga.provider;

import com.mengstudy.boot.tx.saga.dto.SagaRequest;
import com.mengstudy.boot.tx.saga.interceptor.SimpleTransactionContext;
import com.mengstudy.boot.tx.saga.interceptor.SimpleTransactionUtils;
import com.mengstudy.boot.tx.saga.interceptor.SubTransaction;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * Created on 2021/9/24 9:23 .<br>
 *
 * @author gary.fu
 */
public class DefaultSimpleTransactionProviderImpl implements SimpleTransactionProvider {

    @Getter
    @Setter
    private RestTemplate restTemplate;

    @Getter
    @Setter
    private String baseUrl;

    @Override
    public void startSimpleTransaction(SimpleTransactionContext context) {
        SagaRequest request = SimpleTransactionUtils.toSagaRequest(context);
        restTemplate.postForObject(baseUrl + "/startTransaction", request, Map.class);
    }

    @Override
    public void endSimpleTransaction(SimpleTransactionContext context) {
        SagaRequest request = SimpleTransactionUtils.toSagaRequest(context);
        restTemplate.postForObject(baseUrl + "/endTransaction", request, Map.class);
    }

    @Override
    public void startSubTransaction(SubTransaction transaction) {
        SagaRequest request = SimpleTransactionUtils.toSagaRequest(transaction);
        restTemplate.postForObject(baseUrl + "/startSubTransaction", request, Map.class);
    }

    @Override
    public void endSubTransaction(SubTransaction transaction) {
        SagaRequest request = SimpleTransactionUtils.toSagaRequest(transaction);
        restTemplate.postForObject(baseUrl + "/endSubTransaction", request, Map.class);
    }
}

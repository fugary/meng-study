package com.mengstudy.boot.tx.saga.provider.rest;

import com.mengstudy.boot.tx.saga.dto.SagaSimpleTransaction;
import com.mengstudy.boot.tx.saga.interceptor.SimpleTransactionContext;
import com.mengstudy.boot.tx.saga.interceptor.SimpleTransactionUtils;
import com.mengstudy.boot.tx.saga.interceptor.SubTransactionContext;
import com.mengstudy.boot.tx.saga.provider.AbstractSimpleTransactionProvider;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created on 2021/9/24 9:23 .<br>
 *
 * @author gary.fu
 */
public class RestSimpleTransactionProviderImpl extends AbstractSimpleTransactionProvider {

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
    public void recordSubTransaction(SubTransactionContext transaction) {
        SagaRequest request = SimpleTransactionUtils.toSagaSubRequest(transaction);
        restTemplate.postForObject(baseUrl + "/recordSubTransaction", request, Map.class);
    }

    @Override
    public List<SagaSimpleTransaction> loadFailed(List<String> keys) {
        SagaFailedRequest request = new SagaFailedRequest();
        request.setKeys(keys);
        SagaListResult result = restTemplate.postForObject(baseUrl + "/loadFailed", request, SagaListResult.class);
        List<SagaSimpleTransaction> transactions = new ArrayList<>();
        if (result != null && result.isSuccess()) {
            transactions = result.getTransactions();
        }
        return transactions;
    }

    @Override
    public void cancelSimpleTransaction(SagaSimpleTransaction simpleTransaction) {

    }

    @Override
    public void updateSimpleTransaction(SagaSimpleTransaction simpleTransaction) {

    }
}

package com.mengstudy.boot.tx.saga.provider.memory;

import com.mengstudy.boot.tx.saga.dto.SagaSimpleSubTransaction;
import com.mengstudy.boot.tx.saga.dto.SagaSimpleTransaction;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Gary Fu
 * @date 2021/9/25 19:50
 */
@Data
public class MemorySimpleTransaction implements Serializable {

    private SagaSimpleTransaction transaction;

    private List<SagaSimpleSubTransaction> subTransactions = new ArrayList<>();
}

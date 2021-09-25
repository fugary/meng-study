package com.mengstudy.boot.tx.saga.provider.rest;

import com.mengstudy.boot.tx.saga.dto.SagaSimpleTransaction;
import lombok.Data;

import java.io.Serializable;

/**
 * Created on 2021/9/23 16:20 .<br>
 *
 * @author gary.fu
 */
@Data
public class SagaRequest implements Serializable {

    private SagaSimpleTransaction transaction;

}

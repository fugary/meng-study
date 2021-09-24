package com.mengstudy.boot.tx.saga.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Created on 2021/9/24 13:33 .<br>
 *
 * @author gary.fu
 */
@Data
public class SagaFailedRequest implements Serializable {

    private static final long serialVersionUID = -4110519265964035657L;

    private List<String> keys;
}

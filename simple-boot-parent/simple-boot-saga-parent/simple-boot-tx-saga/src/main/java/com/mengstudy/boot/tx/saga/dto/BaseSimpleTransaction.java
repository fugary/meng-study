package com.mengstudy.boot.tx.saga.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created on 2021/9/24 13:22 .<br>
 *
 * @author gary.fu
 */
@Data
public class BaseSimpleTransaction implements Serializable {

    private String txId;

    private String txKey;

    private String txName;

    private Date startDate;

    private Date endDate;

    private Integer status;

    private String message;

}

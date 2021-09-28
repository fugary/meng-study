package com.mengstudy.boot.tx.saga.server.entity.transaction.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * 
 * </p>
 *
 * @author Gary Fu
 * @since 2021-09-28
 */
@TableName("t_simple_transaction_log")
@ApiModel(value = "SimpleTransactionLog对象", description = "")
public class SimpleTransactionLog implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String txId;

    private String txKey;

    private String txName;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private LocalDateTime cancelDate;

    private Integer status;

    private String message;

    private Long txTime;

    private Integer lockStatus;

    private LocalDateTime lockDate;

    private Integer retryTimes;

    private String subTransactionLog;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getTxId() {
        return txId;
    }

    public void setTxId(String txId) {
        this.txId = txId;
    }
    public String getTxKey() {
        return txKey;
    }

    public void setTxKey(String txKey) {
        this.txKey = txKey;
    }
    public String getTxName() {
        return txName;
    }

    public void setTxName(String txName) {
        this.txName = txName;
    }
    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }
    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }
    public LocalDateTime getCancelDate() {
        return cancelDate;
    }

    public void setCancelDate(LocalDateTime cancelDate) {
        this.cancelDate = cancelDate;
    }
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public Long getTxTime() {
        return txTime;
    }

    public void setTxTime(Long txTime) {
        this.txTime = txTime;
    }
    public Integer getLockStatus() {
        return lockStatus;
    }

    public void setLockStatus(Integer lockStatus) {
        this.lockStatus = lockStatus;
    }
    public LocalDateTime getLockDate() {
        return lockDate;
    }

    public void setLockDate(LocalDateTime lockDate) {
        this.lockDate = lockDate;
    }
    public Integer getRetryTimes() {
        return retryTimes;
    }

    public void setRetryTimes(Integer retryTimes) {
        this.retryTimes = retryTimes;
    }
    public String getSubTransactionLog() {
        return subTransactionLog;
    }

    public void setSubTransactionLog(String subTransactionLog) {
        this.subTransactionLog = subTransactionLog;
    }

    @Override
    public String toString() {
        return "SimpleTransactionLog{" +
            "id=" + id +
            ", txId=" + txId +
            ", txKey=" + txKey +
            ", txName=" + txName +
            ", startDate=" + startDate +
            ", endDate=" + endDate +
            ", cancelDate=" + cancelDate +
            ", status=" + status +
            ", message=" + message +
            ", txTime=" + txTime +
            ", lockStatus=" + lockStatus +
            ", lockDate=" + lockDate +
            ", retryTimes=" + retryTimes +
            ", subTransactionLog=" + subTransactionLog +
        "}";
    }
}

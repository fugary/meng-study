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
@TableName("t_simple_sub_transaction")
@ApiModel(value = "SimpleSubTransaction对象", description = "")
public class SimpleSubTransaction implements Serializable {

    private static final long serialVersionUID = 1L;

    private String subTxId;

    private String txId;

    private String txKey;

    private String txName;

    private Integer idxNo;

    private String cancelMethod;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private LocalDateTime cancelDate;

    private Integer status;

    private String message;

    private String serviceName;

    private String serviceClass;

    private String paramClazz;

    private String paramData;

    public String getSubTxId() {
        return subTxId;
    }

    public void setSubTxId(String subTxId) {
        this.subTxId = subTxId;
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
    public Integer getIdxNo() {
        return idxNo;
    }

    public void setIdxNo(Integer idxNo) {
        this.idxNo = idxNo;
    }
    public String getCancelMethod() {
        return cancelMethod;
    }

    public void setCancelMethod(String cancelMethod) {
        this.cancelMethod = cancelMethod;
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
    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
    public String getServiceClass() {
        return serviceClass;
    }

    public void setServiceClass(String serviceClass) {
        this.serviceClass = serviceClass;
    }
    public String getParamClazz() {
        return paramClazz;
    }

    public void setParamClazz(String paramClazz) {
        this.paramClazz = paramClazz;
    }
    public String getParamData() {
        return paramData;
    }

    public void setParamData(String paramData) {
        this.paramData = paramData;
    }

    @Override
    public String toString() {
        return "SimpleSubTransaction{" +
            "subTxId=" + subTxId +
            ", txId=" + txId +
            ", txKey=" + txKey +
            ", txName=" + txName +
            ", idxNo=" + idxNo +
            ", cancelMethod=" + cancelMethod +
            ", startDate=" + startDate +
            ", endDate=" + endDate +
            ", cancelDate=" + cancelDate +
            ", status=" + status +
            ", message=" + message +
            ", serviceName=" + serviceName +
            ", serviceClass=" + serviceClass +
            ", paramClazz=" + paramClazz +
            ", paramData=" + paramData +
        "}";
    }
}

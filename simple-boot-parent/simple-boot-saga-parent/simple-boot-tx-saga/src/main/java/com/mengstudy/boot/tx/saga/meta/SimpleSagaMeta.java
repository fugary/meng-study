package com.mengstudy.boot.tx.saga.meta;

import lombok.Data;

/**
 * @author Gary Fu
 * @date 2021/9/25 18:16
 */
@Data
public class SimpleSagaMeta {

    private String name;

    private String serviceKey;

    private String serviceName;

    private String serviceClass;

    private String cancelMethod;

    private Object targetBean;

}

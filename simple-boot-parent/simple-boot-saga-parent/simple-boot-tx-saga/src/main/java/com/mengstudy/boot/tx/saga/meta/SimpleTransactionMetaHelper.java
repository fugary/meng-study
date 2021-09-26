package com.mengstudy.boot.tx.saga.meta;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Gary Fu
 * @date 2021/9/25 18:18
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SimpleTransactionMetaHelper {

    private static final List<SimpleSagaMeta> METAS = new CopyOnWriteArrayList<>();

    public static SimpleSagaMeta getSageMeta(String serviceKey) {
        for (SimpleSagaMeta meta : METAS) {
            if (Objects.equals(serviceKey, meta.getServiceKey())) {
                return meta;
            }
        }
        return null;
    }

    public static void addSagaMeta(SimpleSagaMeta meta) {
        METAS.add(meta);
    }

}

package com.mengstudy.boot.tx.saga.starter.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created on 2021/9/26 16:37 .<br>
 *
 * @author gary.fu
 */
@ConfigurationProperties(prefix = "simple.transaction")
@Data
public class SimpleTransactionConfigProperties {

    private Saga saga = new Saga();

    @Data
    public static class Saga {
        private boolean enabled = true;
        private Integer delayTime = 30;
        private Integer period = 10;
        private Integer pageSize = 50;
        private Integer retryTimes = 3;
        private TransactionProviderType provider = TransactionProviderType.IN_MEMORY;
    }

}

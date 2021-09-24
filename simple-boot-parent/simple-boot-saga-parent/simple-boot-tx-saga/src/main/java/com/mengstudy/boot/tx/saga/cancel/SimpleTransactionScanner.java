package com.mengstudy.boot.tx.saga.cancel;

import com.mengstudy.boot.tx.saga.annotation.SimpleTransactional;
import com.mengstudy.boot.tx.saga.provider.SimpleTransactionProvider;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created on 2021/9/24 11:41 .<br>
 *
 * @author gary.fu
 */
public class SimpleTransactionScanner implements BeanPostProcessor {

    @Getter
    @Setter
    private ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

    @Getter
    @Setter
    private SimpleTransactionProvider simpleTransactionProvider;

    @Getter
    @Setter
    private long delayTime = 30L;

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<SimpleTransactional> simpleTransactionalClazz = SimpleTransactional.class;
        Set<String> transactionKeys = new HashSet<>();
        Arrays.stream(ReflectionUtils.getDeclaredMethods(bean.getClass()))
                .filter(method -> AnnotationUtils.findAnnotation(method, simpleTransactionalClazz) != null)
                .forEach(method -> {
                    String transactionKey = bean.getClass().getName() + "#" + method.getName();
                    transactionKeys.add(transactionKey);
                });
        if (!transactionKeys.isEmpty()) {
            SimpleTransactionCancelTask cancelTask = new SimpleTransactionCancelTask(simpleTransactionProvider, new ArrayList<>(transactionKeys));
            scheduledExecutorService.schedule(cancelTask, getDelayTime(), TimeUnit.SECONDS);
        }
        return bean;
    }
}

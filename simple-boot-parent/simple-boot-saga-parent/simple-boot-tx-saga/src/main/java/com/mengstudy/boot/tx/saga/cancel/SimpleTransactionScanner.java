package com.mengstudy.boot.tx.saga.cancel;

import com.mengstudy.boot.tx.saga.annotation.SimpleSaga;
import com.mengstudy.boot.tx.saga.annotation.SimpleTransactional;
import com.mengstudy.boot.tx.saga.interceptor.SimpleTransactionUtils;
import com.mengstudy.boot.tx.saga.meta.SimpleSagaMeta;
import com.mengstudy.boot.tx.saga.meta.SimpleTransactionMetaHelper;
import com.mengstudy.boot.tx.saga.provider.SimpleTransactionProvider;
import lombok.Getter;
import lombok.Setter;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
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

    @Getter
    @Setter
    private long period = 30L;

    @Getter
    @Setter
    private SimpleTransactionCancelTask simpleTransactionCancelTask;

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<SimpleTransactional> simpleTransactionalClazz = SimpleTransactional.class;
        Class<SimpleSaga> simpleSagaClass = SimpleSaga.class;
        Set<String> transactionKeys = new HashSet<>();
        Arrays.stream(ReflectionUtils.getDeclaredMethods(bean.getClass()))
                .forEach(method -> {
                    SimpleTransactional simpleTransactional = AnnotationUtils.findAnnotation(method, simpleTransactionalClazz);
                    if (simpleTransactional != null) {
                        Class<?> targetClass = AopUtils.getTargetClass(bean);
                        String transactionKey = SimpleTransactionUtils.getTxKey(targetClass, method);
                        transactionKeys.add(transactionKey);
                    }
                    SimpleSaga simpleSaga = AnnotationUtils.findAnnotation(method, simpleSagaClass);
                    if (simpleSaga != null) {
                        Class<?> targetClass = AopUtils.getTargetClass(bean);
                        String transactionKey = SimpleTransactionUtils.getTxKey(targetClass, method);
                        SimpleSagaMeta simpleSagaMeta = new SimpleSagaMeta();
                        simpleSagaMeta.setServiceClass(targetClass.getName());
                        simpleSagaMeta.setServiceKey(transactionKey);
                        simpleSagaMeta.setServiceName(beanName);
                        simpleSagaMeta.setMethodKey(SimpleTransactionUtils.getMethodKey(method));
                        simpleSagaMeta.setCancelMethod(simpleSaga.cancelMethod());
                        simpleSagaMeta.setName(simpleSaga.name());
                        simpleSagaMeta.setTargetBean(bean);
                        SimpleTransactionMetaHelper.addSagaMeta(simpleSagaMeta);
                    }
                });
        if (!transactionKeys.isEmpty()) {
            addOrInitCancelTask(transactionKeys);
        }
        return bean;
    }

    protected synchronized void addOrInitCancelTask(Set<String> transactionKeys) {
        if (simpleTransactionCancelTask == null) {
            simpleTransactionCancelTask = new SimpleTransactionCancelTask(simpleTransactionProvider, new ConcurrentSkipListSet<>());
            scheduledExecutorService.scheduleAtFixedRate(simpleTransactionCancelTask, getDelayTime(), getPeriod(), TimeUnit.SECONDS);
        }
        simpleTransactionCancelTask.getTransactionKeys().addAll(transactionKeys);
    }
}

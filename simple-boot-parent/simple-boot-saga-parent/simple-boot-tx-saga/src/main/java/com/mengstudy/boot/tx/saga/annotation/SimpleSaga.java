package com.mengstudy.boot.tx.saga.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created on 2021/9/23 16:03 .<br>
 *
 * @author gary.fu
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface SimpleSaga {

    String cancelMethod() default "doCancel";
}

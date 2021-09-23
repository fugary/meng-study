package com.mengstudy.boot.tx.saga.annotation;

/**
 * Created on 2021/9/23 16:03 .<br>
 *
 * @author gary.fu
 */
public @interface SimpleSaga {

    String cancelMethod() default "doCancel";
}

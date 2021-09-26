package com.mengstudy.boot.tx.saga.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Created on 2021/9/26 11:05 .<br>
 *
 * @author gary.fu
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SimpleTransactionConstant {

    public static final Integer STATUS_PENDING = 0;

    public static final Integer STATUS_SUCCESS = 1; // 成功终态

    public static final Integer STATUS_FAILED = 2;

    public static final Integer STATUS_CANCEL_FAILED0 = 8; // 取消失败

    public static final Integer STATUS_CANCELED = 9;  // 取消终态

    public static final Integer STATUS_CANCEL_FAILED = 10; // 失败终态，人工处理

}

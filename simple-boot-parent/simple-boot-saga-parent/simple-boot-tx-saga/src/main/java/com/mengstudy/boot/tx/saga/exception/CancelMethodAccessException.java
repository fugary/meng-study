package com.mengstudy.boot.tx.saga.exception;

/**
 * Created on 2021/9/26 15:46 .<br>
 *
 * @author gary.fu
 */
public class CancelMethodAccessException extends RuntimeException {

    public CancelMethodAccessException() {
    }

    public CancelMethodAccessException(String message) {
        super(message);
    }

    public CancelMethodAccessException(String message, Throwable cause) {
        super(message, cause);
    }

    public CancelMethodAccessException(Throwable cause) {
        super(cause);
    }

    public CancelMethodAccessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

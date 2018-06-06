package com.jonahseguin.papaya.exception;

/**
 * Created by Jonah on 5/30/2018.
 * Project: papaya
 *
 * @ 5:40 PM
 */
public class PapayaCheckException extends RuntimeException {

    public PapayaCheckException() {
    }

    public PapayaCheckException(String message) {
        super(message);
    }

    public PapayaCheckException(String message, Throwable cause) {
        super(message, cause);
    }

    public PapayaCheckException(Throwable cause) {
        super(cause);
    }

    public PapayaCheckException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

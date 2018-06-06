package com.jonahseguin.papaya.backend.config.exceptions;

/**
 * Created by Jonah on 10/15/2017.
 * Project: purifiedCore
 *
 * @ 6:32 PM
 */
public class SerializeObjectNotStringException extends RuntimeException {

    public SerializeObjectNotStringException() {
    }

    public SerializeObjectNotStringException(String message) {
        super(message);
    }

    public SerializeObjectNotStringException(String message, Throwable cause) {
        super(message, cause);
    }

    public SerializeObjectNotStringException(Throwable cause) {
        super(cause);
    }

    public SerializeObjectNotStringException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

package com.jonahseguin.papaya.exception;

/**
 * Created by Jonah on 5/30/2018.
 * Project: papaya
 *
 * @ 9:38 AM
 */
public class WorldNotFoundException extends RuntimeException {

    public WorldNotFoundException() {
    }

    public WorldNotFoundException(String message) {
        super(message);
    }

    public WorldNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public WorldNotFoundException(Throwable cause) {
        super(cause);
    }

    public WorldNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

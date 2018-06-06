package com.jonahseguin.papaya.util.error;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/**
 * Created by Jonah on 10/15/2017.
 * Project: purifiedCore
 *
 * @ 5:08 PM
 */
@Getter
@Setter
public class ErrorRecord {

    private final Exception exception;
    private final String message;
    private final long time;
    private final String id; // UUID
    private boolean read = false;

    public ErrorRecord(Exception exception, String message, long time) {
        this.exception = exception;
        this.message = message;
        this.time = time;
        this.id = UUID.randomUUID().toString().toLowerCase();
    }

    public boolean isUnread() {
        return !read;
    }

    public void markAsRead() {
        this.read = true;
    }

}

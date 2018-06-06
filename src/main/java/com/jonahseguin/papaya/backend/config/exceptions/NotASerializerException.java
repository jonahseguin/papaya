/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.papaya.backend.config.exceptions;


public class NotASerializerException extends Exception {

    public NotASerializerException() {
        super("Config given an object which doesn't extend serializer");
    }
}

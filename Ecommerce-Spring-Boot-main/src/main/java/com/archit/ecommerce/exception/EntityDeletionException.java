package com.archit.ecommerce.exception;

public class EntityDeletionException extends RuntimeException {
    public EntityDeletionException(String message) {
        super(message);
    }
}

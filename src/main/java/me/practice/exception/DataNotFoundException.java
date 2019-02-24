package me.practice.exception;


public class DataNotFoundException extends RuntimeException {
    public DataNotFoundException(String entity, Integer id) {
        super("Count not found " + entity + " with id " + Integer.toString(id));
    }
}

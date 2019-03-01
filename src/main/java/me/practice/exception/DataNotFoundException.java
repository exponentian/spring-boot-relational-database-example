package me.practice.exception;


public class DataNotFoundException extends RuntimeException {
    public DataNotFoundException(String entity, Integer id) {
        super("Could not found " + entity + " with id " + Integer.toString(id));
    }
}

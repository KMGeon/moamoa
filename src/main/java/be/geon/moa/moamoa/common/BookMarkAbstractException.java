package be.geon.moa.moamoa.common;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class BookMarkAbstractException extends RuntimeException{
    public final Map<String, String> validation = new HashMap<>();

    public BookMarkAbstractException(String message) {
        super(message);
    }

    public BookMarkAbstractException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract String getStatusCode();

    public void addValidation(String fieldName, String message) {
        validation.put(fieldName, message);
    }
}
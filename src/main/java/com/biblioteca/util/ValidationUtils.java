package com.biblioteca.util;

public class ValidationUtils {

    public static void assertNotNull(Object value, String message) {
        if (value == null) {
            throw new IllegalArgumentException(message);
        }
    }
}

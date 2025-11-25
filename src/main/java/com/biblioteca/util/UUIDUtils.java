package com.biblioteca.util;

import java.util.UUID;

public class UUIDUtils {

    public static String gerarCodigo() {
        return UUID.randomUUID().toString();
    }
}

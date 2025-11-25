package com.biblioteca.util;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class DateUtils {

    public static long diasEntre(LocalDate inicio, LocalDate fim) {
        return ChronoUnit.DAYS.between(inicio, fim);
    }
}

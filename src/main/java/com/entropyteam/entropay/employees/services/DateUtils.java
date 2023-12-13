package com.entropyteam.entropay.employees.services;

import java.time.LocalDate;
import org.springframework.lang.NonNull;

public class DateUtils {

    public static boolean isDocumentActive(@NonNull LocalDate startDate, LocalDate endDate) {
        LocalDate today = LocalDate.now();
        if (endDate == null) {
            return (startDate.isBefore(today) || startDate.isEqual(today));
        } else {
            return (startDate.isBefore(today) || startDate.isEqual(today)) &&
                    (endDate.isAfter(today) || endDate.isEqual(today));
        }
    }

}

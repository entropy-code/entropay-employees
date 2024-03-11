package com.entropyteam.entropay.common;

import com.entropyteam.entropay.employees.models.Vacation;

import java.io.InputStream;
import java.io.StringWriter;
import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class BuilderUtils {

    public static InputStream convertMapToCSVInputStream(Map<String, Integer> summary) throws IOException {
        StringWriter writer = new StringWriter();
        writer.write(" Vacation summary of " + LocalDate.now() + "\n");
        writer.write("\n");

        if (!summary.isEmpty()) {
            writer.write("Employee,Days" + "\n");
            for (Map.Entry<String, Integer> entry : summary.entrySet()) {
                writer.write(entry.getKey() + "," + entry.getValue() + "\n");
            }
        } else {
            writer.write("No employees found to add vacations");
        }

        // Convert the StringWriter to an InputStream and return
        return new ByteArrayInputStream(writer.toString().getBytes());
    }

    public static InputStream convertVacationToCSVInputStream(List<Vacation> summary) throws IOException {
        StringWriter writer = new StringWriter();
        writer.write(" Vacation summary of " + LocalDate.now() + "\n");
        writer.write("\n");

        if (!summary.isEmpty()) {
            writer.write("Employee,Days,Year" + "\n");
            for (Vacation vacation : summary) {
                writer.write(vacation.getEmployee().getFullName() + "," + vacation.getDebit() + "," + vacation.getYear() + "\n");
            }
        } else {
            writer.write("No employees found to add vacations");
        }

        // Convert the StringWriter to an InputStream and return
        return new ByteArrayInputStream(writer.toString().getBytes());
    }
}
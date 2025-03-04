package com.entropyteam.entropay.employees.dtos;


import java.util.List;

public record ReportDto<T>(List<T> data, int size) {

}
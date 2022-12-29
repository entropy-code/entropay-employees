package com.entropyteam.entropay.employees.dtos;

import java.util.List;

public record PermissionDto(String entity,
                            List<String> actions) {

}

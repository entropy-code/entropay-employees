package com.entropyteam.entropay.common;

import java.util.Map;

public record ReactAdminSqlParams(Map<String, String> queryParameters, int limit, int offset, String sort,
                                  String order) {

}

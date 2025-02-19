package com.entropyteam.entropay.security.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LeakResponseDto {
    private boolean success;
    private List<LeakDto> result;

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public List<LeakDto> getResult() { return result; }
    public void setResult(List<LeakDto> result) { this.result = result; }
}
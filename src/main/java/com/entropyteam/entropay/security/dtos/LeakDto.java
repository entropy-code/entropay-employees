package com.entropyteam.entropay.security.dtos;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LeakDto {
    private String email;
    private SourceDto source;
    private String password;
    private List<String> origin;

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public SourceDto getSource() { return source; }
    public void setSource(SourceDto source) { this.source = source; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public List<String> getOrigin() { return origin; }
    public void setOrigin(List<String> origin) { this.origin = origin; }
}

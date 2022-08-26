package com.entropyteam.entropay.employees.models;

import javax.persistence.Entity;
import javax.persistence.Table;
import com.entropyteam.entropay.common.BaseEntity;
import com.entropyteam.entropay.employees.dtos.TenantDto;

@Entity
@Table(name = "tenant")
public class Tenant extends BaseEntity {
    private String name;
    private String displayName;

    public Tenant() {
    }

    public Tenant(TenantDto entity) {
        this.name = entity.name();
        this.displayName = entity.displayName();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}

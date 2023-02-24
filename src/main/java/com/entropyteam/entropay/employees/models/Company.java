package com.entropyteam.entropay.employees.models;

import java.util.UUID;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import com.entropyteam.entropay.common.BaseEntity;
import com.entropyteam.entropay.employees.dtos.CompanyDto;

@Entity
@Table(name = "company")
public class Company extends BaseEntity {
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id")
    private Tenant tenant;

    @Embedded
    private Address address;

    public Company() {
    }

    public Company(CompanyDto entity) {
        this.name = entity.name();
        address = new Address();
        address.setCountry(entity.country());
        address.setCity(entity.city());
        address.setState(entity.state());
        address.setZipCode(entity.zipCode());
        address.setAddressLine(entity.address());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getAddressLine() {
        return address != null ? address.getAddressLine() : null;
    }

    public String getZipCode() {
        return address != null ? address.getZipCode() : null;
    }

    public String getState() {
        return address != null ? address.getState() : null;
    }

    public String getCountry() {
        return address != null ? address.getCountry() : null;
    }

    public String getCity() {
        return address != null ? address.getCity() : null;
    }

    public UUID getTenantId() {
        return tenant.getId();
    }
}

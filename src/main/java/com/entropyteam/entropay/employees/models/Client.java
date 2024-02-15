package com.entropyteam.entropay.employees.models;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import com.entropyteam.entropay.common.BaseEntity;
import com.entropyteam.entropay.employees.dtos.ClientDto;

@Entity(name = "Client")
@Table(name = "client")
public class Client extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    @Column(nullable = false)
    private String name;

    @Embedded
    private Address address;

    @Column
    private String contactFullName;

    private String contactEmail;

    private String preferredCurrency;
    private String internalId;

    public Client(ClientDto clientDto) {
        this.name = clientDto.name();
        this.contactFullName = clientDto.contactFullName();
        this.contactEmail = clientDto.contactEmail();
        this.preferredCurrency = clientDto.preferredCurrency();
        this.internalId = clientDto.internalId();
        address = new Address();
        address.setCountry(clientDto.country());
        address.setState(clientDto.state());
        address.setCity(clientDto.city());
        address.setZipCode(clientDto.zipCode());
        address.setAddressLine(clientDto.address());
    }

    public Client() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getContactFullName() {
        return contactFullName;
    }

    public void setContactFullName(String contactFullName) {
        this.contactFullName = contactFullName;
    }

    public String getPreferredCurrency() {
        return preferredCurrency;
    }

    public void setPreferredCurrency(String preferredCurrency) {
        this.preferredCurrency = preferredCurrency;
    }

    public String getCountry() {
        return address != null ? address.getCountry() : null;
    }

    public String getState() {
        return address != null ? address.getState() : null;
    }

    public String getCity() {
        return address != null ? address.getCity() : null;
    }

    public String getZipCode() {
        return address != null? address.getZipCode() : null;
    }

    public String getAddressLine() {
        return address != null ? address.getAddressLine() : null;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public UUID getCompanyId() {
        return company.getId();
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public void setInternalId(String internalId) {
        this.internalId = internalId;
    }

    public String getInternalId() {
        return internalId;
    }
}

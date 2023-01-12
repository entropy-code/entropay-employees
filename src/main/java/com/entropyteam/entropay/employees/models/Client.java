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

    @Column(unique = true, nullable = false)
    private String name;

    @Embedded
    private Address address;

    @Column
    private String contactFullName;

    private String contactEmail;

    @Column(nullable = false)
    private String preferredCurrency;

    public Client(ClientDto clientDto) {
        this.name = clientDto.name();
        this.contactFullName = clientDto.contactFullName();
        this.contactEmail = clientDto.contactEmail();
        this.preferredCurrency = clientDto.preferredCurrency();
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
        return address.getCountry();
    }

    public String getState() {
        return address.getState();
    }

    public String getCity() {
        return address.getCity();
    }

    public String getZipCode() {
        return address.getZipCode();
    }

    public String getAddressLine() {
        return address.getAddressLine();
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
}

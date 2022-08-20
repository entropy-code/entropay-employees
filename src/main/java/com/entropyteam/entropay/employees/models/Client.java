package com.entropyteam.entropay.employees.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.entropyteam.entropay.common.BaseEntity;
import com.entropyteam.entropay.employees.dtos.ClientDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "Client")
@Table(name = "client")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Client extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String name;

    @Column
    private String address;

    @Column
    private String zipCode;

    @Column
    private String city;

    @Column
    private String state;

    @Column
    private String country;

    @Column
    private String contact;

    @Column(nullable = false)
    private String preferredCurrency;

    public Client(ClientDto clientDto) {
        this.name = clientDto.name();
        this.address = clientDto.address();
        this.zipCode = clientDto.zipCode();
        this.city = clientDto.city();
        this.state = clientDto.state();
        this.country = clientDto.country();
        this.contact = clientDto.contact();
        this.preferredCurrency = clientDto.preferredCurrency();
    }
}

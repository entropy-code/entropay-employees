package com.entropyteam.entropay.employees.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.entropyteam.entropay.common.BaseEntity;
import com.entropyteam.entropay.employees.dtos.CountryDto;

@Entity(name = "Country")
@Table(name = "country")
public class Country extends BaseEntity {

    @Column
    private String name;

    public Country() {
    }

    public Country(CountryDto entity) {
        this.name = entity.name();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
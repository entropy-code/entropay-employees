package com.entropyteam.entropay.employees.models;

import com.entropyteam.entropay.common.BaseEntity;
import com.entropyteam.entropay.employees.dtos.HolidayDto;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "holiday_calendar")
public class Holiday extends BaseEntity {

    private LocalDate date;
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id")
    private Country country;

    public Holiday(){

    }

    public Holiday(HolidayDto holidayDto){
        this.date = holidayDto.date();
        this.description = holidayDto.description();
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }
}

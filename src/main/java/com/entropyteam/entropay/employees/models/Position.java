package com.entropyteam.entropay.employees.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.entropyteam.entropay.common.BaseEntity;
import com.entropyteam.entropay.employees.dtos.PositionDto;

@Entity(name = "Position")
@Table(name = "position")
public class Position extends BaseEntity {

    @Column
    private String name;

    public Position() {
    }

    public Position(PositionDto entity) {
        this.name = entity.name();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

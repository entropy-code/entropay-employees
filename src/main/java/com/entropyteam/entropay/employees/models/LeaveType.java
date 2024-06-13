package com.entropyteam.entropay.employees.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import com.entropyteam.entropay.common.BaseEntity;
import com.entropyteam.entropay.employees.dtos.LeaveTypeDto;

@Entity(name = "LeaveType")
@Table(name = "leave_type")
public class LeaveType extends BaseEntity {

    @Column
    private String name;

    public LeaveType() {
    }

    public LeaveType(LeaveTypeDto entity) {
        this.name = entity.name();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

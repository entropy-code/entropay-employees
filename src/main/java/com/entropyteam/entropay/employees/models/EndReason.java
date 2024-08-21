package com.entropyteam.entropay.employees.models;

import java.util.Set;
import com.entropyteam.entropay.common.BaseEntity;
import com.entropyteam.entropay.employees.dtos.EndReasonDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity (name = "EndReason")
@Table(name = "end_reason")
public class EndReason extends BaseEntity {

    @Column
    private String name;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "endReason")
    private Set<Contract> contracts;

    public EndReason(){
    }

    public EndReason(EndReasonDto entity)   { this.name = entity.name(); }

    public String getName() { return name; }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Contract> getContracts() {
        return contracts;
    }

    public void setContracts(Set<Contract> contracts) {
        this.contracts = contracts;
    }
}

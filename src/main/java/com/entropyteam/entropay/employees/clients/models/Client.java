package com.entropyteam.entropay.employees.clients.models;

import java.time.LocalDateTime;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "clients")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Client {

    @Id
    @GeneratedValue
    private UUID id;

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

    @CreationTimestamp
    @Column(columnDefinition = "TIMESTAMP", nullable = false, updatable = false)
    private LocalDateTime createdOn;

    @UpdateTimestamp
    @Column(columnDefinition = "TIMESTAMP", nullable = false)
    private LocalDateTime modifiedOn;

    @Column
    private boolean isActive;
}

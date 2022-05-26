package com.entropyteam.entropay.employees.clients.models;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "clients", schema = "employees")
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
    private String contact;

    @Column(nullable = false)
    private String preferredCurrency;

    @CreationTimestamp
    @Column(columnDefinition = "TIMESTAMP", nullable = false, updatable = false)
    private LocalDateTime createdOn;

    @UpdateTimestamp
    @Column(columnDefinition = "TIMESTAMP", nullable = false)
    private LocalDateTime modifiedOn;

    @Column(nullable = false)
    private boolean isActive;
}

package com.entropy.entropay.employees.clients.models;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

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
    private String contact;

    @Column(nullable = false)
    private String preferredCurrency;

    @Column(columnDefinition = "TIMESTAMP", nullable = false, updatable = false)
    private LocalDateTime createdOn;

    @Column(columnDefinition = "TIMESTAMP", nullable = false)
    private LocalDateTime modifiedOn;

    @Column(nullable = false)
    private boolean isActive;
}

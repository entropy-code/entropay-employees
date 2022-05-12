package com.entropy.entropay.employees.projects.models;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import com.entropy.entropay.employees.clients.models.Client;

import lombok.Data;

@Entity
@Table(name = "projects")
@Data
public class Project {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne(cascade = CascadeType.PERSIST, optional = false)
    @JoinColumn(name = "project_type_id", nullable = false)
    private ProjectType projectType;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TIMESTAMP", nullable = false)
    private LocalDateTime startDate;

    @Column(columnDefinition = "TIMESTAMP", nullable = false)
    private LocalDateTime endDate;

    @Column(nullable = false)
    private String notes;

    @Column(columnDefinition = "TIMESTAMP", nullable = false, updatable = false)
    private LocalDateTime createdOn;

    @Column(columnDefinition = "TIMESTAMP", nullable = false)
    private LocalDateTime modifiedOn;

}

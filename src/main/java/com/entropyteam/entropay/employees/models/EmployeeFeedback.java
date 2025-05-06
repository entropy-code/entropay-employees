package com.entropyteam.entropay.employees.models;

import java.time.LocalDate;
import com.entropyteam.entropay.common.BaseEntity;
import com.entropyteam.entropay.employees.dtos.FeedbackDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity(name = "EmployeeFeedback")
@Table(name = "employee_feedback")
public class EmployeeFeedback extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(nullable = false)
    private String createdBy;

    @Column(nullable = false)
    private LocalDate feedbackDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FeedbackSource source;

    @Column(nullable = false)
    private String title;

    @Lob
    @Column(columnDefinition = "TEXT", nullable = false)
    private String text;

    public EmployeeFeedback() {
    }

    public EmployeeFeedback(FeedbackDto dto, Employee employee) {
        this.employee = employee;
        this.createdBy = dto.createdBy();
        this.feedbackDate = dto.feedbackDate();
        this.source = dto.source();
        this.title = dto.title();
        this.text = dto.text();
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDate getFeedbackDate() {
        return feedbackDate;
    }

    public void setFeedbackDate(LocalDate feedbackDate) {
        this.feedbackDate = feedbackDate;
    }

    public FeedbackSource getSource() {
        return source;
    }

    public void setSource(FeedbackSource source) {
        this.source = source;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}

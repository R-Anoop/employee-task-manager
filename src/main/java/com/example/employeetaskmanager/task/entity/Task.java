package com.example.employeetaskmanager.task.entity;

import com.example.employeetaskmanager.employee.entity.Employee;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(length = 2000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TaskStatus status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_employee_id")
    private Employee assignedEmployee;

    protected Task() {
    }

    public Task(String title, String description, Employee assignedEmployee) {
        this.title = title;
        this.description = description;
        this.status = TaskStatus.TODO;
        this.assignedEmployee = assignedEmployee;
    }

    @PrePersist
    private void setCreationDate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Employee getAssignedEmployee() {
        return assignedEmployee;
    }

    public void updateDetails(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public void updateStatus(TaskStatus status) {
        this.status = status;
    }

    public void assignTo(Employee employee) {
        this.assignedEmployee = employee;
    }
}

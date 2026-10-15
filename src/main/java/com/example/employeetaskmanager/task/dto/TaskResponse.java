package com.example.employeetaskmanager.task.dto;

import com.example.employeetaskmanager.task.entity.TaskStatus;

import java.time.LocalDateTime;

public record TaskResponse(
        Long id,
        String title,
        String description,
        TaskStatus status,
        LocalDateTime createdAt,
        Long assignedEmployeeId,
        String assignedEmployeeName
) {
}

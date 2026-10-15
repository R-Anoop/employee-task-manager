package com.example.employeetaskmanager.task.dto;

import com.example.employeetaskmanager.task.entity.TaskStatus;
import jakarta.validation.constraints.NotNull;

public record TaskStatusRequest(
        @NotNull(message = "Status is required")
        TaskStatus status
) {
}

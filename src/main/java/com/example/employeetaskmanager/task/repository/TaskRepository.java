package com.example.employeetaskmanager.task.repository;

import com.example.employeetaskmanager.task.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}

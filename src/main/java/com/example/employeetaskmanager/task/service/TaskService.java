package com.example.employeetaskmanager.task.service;

import com.example.employeetaskmanager.employee.entity.Employee;
import com.example.employeetaskmanager.employee.exception.EmployeeNotFoundException;
import com.example.employeetaskmanager.employee.repository.EmployeeRepository;
import com.example.employeetaskmanager.task.dto.TaskRequest;
import com.example.employeetaskmanager.task.dto.TaskResponse;
import com.example.employeetaskmanager.task.entity.Task;
import com.example.employeetaskmanager.task.entity.TaskStatus;
import com.example.employeetaskmanager.task.exception.TaskNotFoundException;
import com.example.employeetaskmanager.task.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TaskService {

    private final TaskRepository taskRepository;
    private final EmployeeRepository employeeRepository;

    public TaskService(TaskRepository taskRepository, EmployeeRepository employeeRepository) {
        this.taskRepository = taskRepository;
        this.employeeRepository = employeeRepository;
    }

    public TaskResponse createTask(TaskRequest request) {
        Employee employee = request.assignedEmployeeId() == null
                ? null
                : findEmployeeById(request.assignedEmployeeId());
        Task task = new Task(request.title(), request.description(), employee);
        return toResponse(taskRepository.save(task));
    }

    @Transactional(readOnly = true)
    public List<TaskResponse> getAllTasks() {
        return taskRepository.findAll().stream()
                .map(TaskService::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public TaskResponse getTaskById(Long id) {
        return toResponse(findTaskById(id));
    }

    public TaskResponse updateTask(Long id, TaskRequest request) {
        Task task = findTaskById(id);
        task.updateDetails(request.title(), request.description());
        if (request.assignedEmployeeId() != null) {
            task.assignTo(findEmployeeById(request.assignedEmployeeId()));
        }
        return toResponse(taskRepository.save(task));
    }

    public TaskResponse updateTaskStatus(Long id, TaskStatus status) {
        Task task = findTaskById(id);
        task.updateStatus(status);
        return toResponse(taskRepository.save(task));
    }

    public TaskResponse assignTask(Long id, Long employeeId) {
        Task task = findTaskById(id);
        task.assignTo(findEmployeeById(employeeId));
        return toResponse(taskRepository.save(task));
    }

    public void deleteTask(Long id) {
        taskRepository.delete(findTaskById(id));
    }

    private Task findTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }

    private Employee findEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));
    }

    private static TaskResponse toResponse(Task task) {
        Employee employee = task.getAssignedEmployee();
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getCreatedAt(),
                employee == null ? null : employee.getId(),
                employee == null ? null : employee.getName()
        );
    }
}

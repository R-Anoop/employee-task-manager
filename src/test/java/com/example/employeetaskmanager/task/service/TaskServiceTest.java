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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private TaskService taskService;

    @Test
    void createTaskSavesTaskAndReturnsResponse() {
        Employee employee = employee(5L);
        Task task = task(1L, employee);
        when(employeeRepository.findById(5L)).thenReturn(Optional.of(employee));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        TaskResponse response = taskService.createTask(request(5L));

        assertEquals(1L, response.id());
        assertEquals("Prepare release", response.title());
        assertEquals(TaskStatus.TODO, response.status());
        assertEquals(5L, response.assignedEmployeeId());
        assertEquals("Ava Chen", response.assignedEmployeeName());
        assertNotNull(response.createdAt());
    }

    @Test
    void createTaskCanBeUnassigned() {
        Task task = task(1L, null);
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        TaskResponse response = taskService.createTask(request(null));

        assertNull(response.assignedEmployeeId());
        assertNull(response.assignedEmployeeName());
    }

    @Test
    void createTaskFailsWhenAssignedEmployeeDoesNotExist() {
        when(employeeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EmployeeNotFoundException.class, () -> taskService.createTask(request(99L)));
    }

    @Test
    void getAllTasksReturnsResponses() {
        when(taskRepository.findAll()).thenReturn(List.of(task(1L, null)));

        List<TaskResponse> responses = taskService.getAllTasks();

        assertEquals(1, responses.size());
        assertEquals("Prepare release", responses.getFirst().title());
    }

    @Test
    void getTaskByIdReturnsResponse() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task(1L, null)));

        TaskResponse response = taskService.getTaskById(1L);

        assertEquals(1L, response.id());
    }

    @Test
    void operationsFailWhenTaskDoesNotExist() {
        when(taskRepository.findById(42L)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> taskService.getTaskById(42L));
        assertThrows(TaskNotFoundException.class, () -> taskService.updateTask(42L, request(null)));
        assertThrows(TaskNotFoundException.class, () -> taskService.updateTaskStatus(42L, TaskStatus.COMPLETED));
        assertThrows(TaskNotFoundException.class, () -> taskService.assignTask(42L, 5L));
        assertThrows(TaskNotFoundException.class, () -> taskService.deleteTask(42L));
    }

    @Test
    void updateTaskChangesDetailsAndCanReassign() {
        Employee employee = employee(5L);
        Task task = task(1L, null);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(employeeRepository.findById(5L)).thenReturn(Optional.of(employee));
        when(taskRepository.save(task)).thenReturn(task);

        TaskResponse response = taskService.updateTask(1L, new TaskRequest(
                "Updated title", "Updated description", 5L
        ));

        assertEquals("Updated title", response.title());
        assertEquals("Updated description", response.description());
        assertEquals(5L, response.assignedEmployeeId());
    }

    @Test
    void updateTaskStatusPersistsStatus() {
        Task task = task(1L, null);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(task)).thenReturn(task);

        TaskResponse response = taskService.updateTaskStatus(1L, TaskStatus.IN_PROGRESS);

        assertEquals(TaskStatus.IN_PROGRESS, response.status());
    }

    @Test
    void assignTaskReassignsToEmployee() {
        Employee employee = employee(5L);
        Task task = task(1L, null);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(employeeRepository.findById(5L)).thenReturn(Optional.of(employee));
        when(taskRepository.save(task)).thenReturn(task);

        TaskResponse response = taskService.assignTask(1L, 5L);

        assertEquals(5L, response.assignedEmployeeId());
    }

    @Test
    void assignTaskFailsWhenEmployeeDoesNotExist() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task(1L, null)));
        when(employeeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EmployeeNotFoundException.class, () -> taskService.assignTask(1L, 99L));
    }

    @Test
    void deleteTaskDeletesExistingTask() {
        Task task = task(1L, null);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        taskService.deleteTask(1L);

        verify(taskRepository).delete(task);
    }

    private static TaskRequest request(Long employeeId) {
        return new TaskRequest("Prepare release", "Prepare the release notes", employeeId);
    }

    private static Employee employee(Long id) {
        Employee employee = new Employee("Ava Chen", "ava@example.com", "Engineering");
        ReflectionTestUtils.setField(employee, "id", id);
        return employee;
    }

    private static Task task(Long id, Employee employee) {
        Task task = new Task("Prepare release", "Prepare the release notes", employee);
        ReflectionTestUtils.setField(task, "id", id);
        ReflectionTestUtils.setField(task, "createdAt", LocalDateTime.now());
        return task;
    }
}

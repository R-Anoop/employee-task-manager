package com.example.employeetaskmanager.employee.service;

import com.example.employeetaskmanager.employee.dto.EmployeeRequest;
import com.example.employeetaskmanager.employee.dto.EmployeeResponse;
import com.example.employeetaskmanager.employee.entity.Employee;
import com.example.employeetaskmanager.employee.exception.EmployeeNotFoundException;
import com.example.employeetaskmanager.employee.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    @Test
    void createEmployeeSavesAndReturnsEmployeeResponse() {
        EmployeeRequest request = new EmployeeRequest("Ava Chen", "ava@example.com", "Engineering");
        Employee savedEmployee = employee(1L, request);
        when(employeeRepository.save(org.mockito.ArgumentMatchers.any(Employee.class)))
                .thenReturn(savedEmployee);

        EmployeeResponse response = employeeService.createEmployee(request);

        assertEquals(new EmployeeResponse(1L, "Ava Chen", "ava@example.com", "Engineering"), response);
    }

    @Test
    void getAllEmployeesReturnsEmployeeResponses() {
        Employee employee = employee(1L, request("Ava Chen", "ava@example.com", "Engineering"));
        when(employeeRepository.findAll()).thenReturn(List.of(employee));

        List<EmployeeResponse> responses = employeeService.getAllEmployees();

        assertEquals(List.of(new EmployeeResponse(
                1L, "Ava Chen", "ava@example.com", "Engineering"
        )), responses);
    }

    @Test
    void getEmployeeByIdReturnsEmployeeResponse() {
        Employee employee = employee(1L, request("Ava Chen", "ava@example.com", "Engineering"));
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        EmployeeResponse response = employeeService.getEmployeeById(1L);

        assertEquals(new EmployeeResponse(1L, "Ava Chen", "ava@example.com", "Engineering"), response);
    }

    @Test
    void updateEmployeeUpdatesAndReturnsEmployeeResponse() {
        Employee employee = employee(1L, request("Ava Chen", "ava@example.com", "Engineering"));
        EmployeeRequest request = request("Ava Chen", "ava.chen@example.com", "Platform");
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeRepository.save(employee)).thenReturn(employee);

        EmployeeResponse response = employeeService.updateEmployee(1L, request);

        assertEquals(new EmployeeResponse(
                1L, "Ava Chen", "ava.chen@example.com", "Platform"
        ), response);
    }

    @Test
    void deleteEmployeeDeletesExistingEmployee() {
        Employee employee = employee(1L, request("Ava Chen", "ava@example.com", "Engineering"));
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        employeeService.deleteEmployee(1L);

        verify(employeeRepository).delete(employee);
    }

    @Test
    void operationsThrowNotFoundWhenEmployeeDoesNotExist() {
        when(employeeRepository.findById(42L)).thenReturn(Optional.empty());

        assertThrows(EmployeeNotFoundException.class, () -> employeeService.getEmployeeById(42L));
        assertThrows(EmployeeNotFoundException.class, () -> employeeService.updateEmployee(
                42L, request("Ava Chen", "ava@example.com", "Engineering")
        ));
        assertThrows(EmployeeNotFoundException.class, () -> employeeService.deleteEmployee(42L));
    }

    private static EmployeeRequest request(String name, String email, String department) {
        return new EmployeeRequest(name, email, department);
    }

    private static Employee employee(Long id, EmployeeRequest request) {
        Employee employee = new Employee(request.name(), request.email(), request.department());
        try {
            var idField = Employee.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(employee, id);
            return employee;
        } catch (ReflectiveOperationException exception) {
            throw new AssertionError("Unable to assign employee ID for test", exception);
        }
    }
}

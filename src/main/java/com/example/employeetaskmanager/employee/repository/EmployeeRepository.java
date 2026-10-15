package com.example.employeetaskmanager.employee.repository;

import com.example.employeetaskmanager.employee.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}

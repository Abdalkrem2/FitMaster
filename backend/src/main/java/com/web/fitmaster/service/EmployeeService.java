package com.web.fitmaster.service;

import com.web.fitmaster.dto.EmployeeDTOs;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;

public interface EmployeeService {

   EmployeeDTOs.EmployeeResponse getEmployees(Pageable pageable);

    EmployeeDTOs.EmployeeDTO createEmployee(EmployeeDTOs.EmployeeRequest employeeDTO);

    EmployeeDTOs.EmployeeDTO updateEmployee(Long id, EmployeeDTOs.@Valid EmployeeUpdateRequest employeeDTO);

    String deleteEmployee(Long id);

    EmployeeDTOs.EmployeeDTO getEmployee(Long id);
}

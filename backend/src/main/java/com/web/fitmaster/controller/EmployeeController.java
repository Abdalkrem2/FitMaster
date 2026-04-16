package com.web.fitmaster.controller;
import com.web.fitmaster.dto.EmployeeDTOs;
import com.web.fitmaster.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("api/employees")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EmployeeDTOs.EmployeeResponse> getAllEmployees(Pageable pageable) {
        EmployeeDTOs.EmployeeResponse EmployeeUsers=employeeService.getAllEmployees(pageable);
        return ResponseEntity.ok(EmployeeUsers);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EmployeeDTOs.EmployeeDTO> createEmployee(@Valid @RequestBody EmployeeDTOs.EmployeeRequest employeeDTO) {
        EmployeeDTOs.EmployeeDTO employee =employeeService.createEmployee(employeeDTO);
        return new ResponseEntity<>(employee, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EmployeeDTOs.EmployeeDTO>updateEmployee(@PathVariable Long id,  @RequestBody EmployeeDTOs.EmployeeUpdateRequest employeeDTO) {
        EmployeeDTOs.EmployeeDTO data=employeeService.updateEmployee(id,employeeDTO);
        return ResponseEntity.ok(data);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteEmployee(@PathVariable Long id) {
        String status =employeeService.deleteEmployee(id);
        return ResponseEntity.ok(status);
    }


}

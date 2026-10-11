package com.example.ems.mapper;

import com.example.ems.dto.EmployeeResponse;
import com.example.ems.entity.Employee;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper {
    public EmployeeResponse toResponse(Employee employee) {
        var roles = employee.getRoles().stream()
            .map(role -> new EmployeeResponse.RoleSummary(role.getId(), role.getName()))
            .collect(java.util.stream.Collectors.toSet());
        return new EmployeeResponse(employee.getId(), employee.getFirstName(), employee.getLastName(), employee.getEmail(),
            employee.getPhone(), employee.getJoiningDate(), employee.getDepartment().getId(), employee.getDepartment().getName(), roles);
    }
}

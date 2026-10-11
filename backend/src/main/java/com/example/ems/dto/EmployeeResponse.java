package com.example.ems.dto;

import java.time.LocalDate;
import java.util.Set;

public record EmployeeResponse(Long id, String firstName, String lastName, String email, String phone,
        LocalDate joiningDate, Long departmentId, String departmentName, Set<RoleSummary> roles) {
    public record RoleSummary(Long id, String name) {}
}

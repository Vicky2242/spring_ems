package com.example.ems.service;

import com.example.ems.dto.EmployeeRequest;
import com.example.ems.dto.EmployeeResponse;
import com.example.ems.entity.Department;
import com.example.ems.entity.Employee;
import com.example.ems.entity.Role;
import com.example.ems.exception.DuplicateResourceException;
import com.example.ems.exception.ResourceNotFoundException;
import com.example.ems.mapper.EmployeeMapper;
import com.example.ems.repository.DepartmentRepository;
import com.example.ems.repository.EmployeeRepository;
import com.example.ems.repository.RoleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class EmployeeService {
    private final EmployeeRepository employees;
    private final DepartmentRepository departments;
    private final RoleRepository roles;
    private final EmployeeMapper mapper;
    public EmployeeService(EmployeeRepository employees, DepartmentRepository departments, RoleRepository roles, EmployeeMapper mapper) {
        this.employees = employees; this.departments = departments; this.roles = roles; this.mapper = mapper;
    }
    @Transactional(readOnly = true)
    public List<EmployeeResponse> findAll() { return employees.findAll().stream().map(mapper::toResponse).toList(); }
    @Transactional(readOnly = true)
    public EmployeeResponse findById(Long id) { return mapper.toResponse(getEmployee(id)); }
    public EmployeeResponse create(EmployeeRequest request) {
        if (employees.existsByEmailIgnoreCase(request.email().trim())) throw new DuplicateResourceException("An employee with this email already exists.");
        Employee employee = new Employee(); apply(employee, request); return mapper.toResponse(employees.save(employee));
    }
    public EmployeeResponse update(Long id, EmployeeRequest request) {
        Employee employee = getEmployee(id);
        if (employees.existsByEmailIgnoreCaseAndIdNot(request.email().trim(), id)) throw new DuplicateResourceException("Another employee already uses this email.");
        apply(employee, request); return mapper.toResponse(employees.save(employee));
    }
    public void delete(Long id) { employees.delete(getEmployee(id)); }
    private Employee getEmployee(Long id) { return employees.findById(id).orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id)); }
    private void apply(Employee employee, EmployeeRequest request) {
        Department department = departments.findById(request.departmentId()).orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + request.departmentId()));
        Set<Long> roleIds = request.roleIds() == null ? Set.of() : request.roleIds();
        List<Role> found = roles.findAllById(roleIds);
        if (found.size() != roleIds.size()) {
            Set<Long> missing = new java.util.HashSet<>(roleIds);
            missing.removeAll(found.stream().map(Role::getId).collect(Collectors.toSet()));
            throw new ResourceNotFoundException("Role IDs not found: " + missing);
        }
        employee.setFirstName(request.firstName().trim()); employee.setLastName(request.lastName().trim());
        employee.setEmail(request.email().trim().toLowerCase());
        employee.setPhone(request.phone() == null || request.phone().isBlank() ? null : request.phone().trim());
        employee.setJoiningDate(request.joiningDate()); employee.setDepartment(department); employee.setRoles(new LinkedHashSet<>(found));
    }
}

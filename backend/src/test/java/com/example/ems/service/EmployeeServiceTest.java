package com.example.ems.service;
import com.example.ems.dto.EmployeeRequest;
import com.example.ems.exception.DuplicateResourceException;
import com.example.ems.mapper.EmployeeMapper;
import com.example.ems.repository.DepartmentRepository;
import com.example.ems.repository.EmployeeRepository;
import com.example.ems.repository.RoleRepository;
import org.junit.jupiter.api.Test;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
class EmployeeServiceTest {
 @Test void createRejectsDuplicateEmail() {
  EmployeeRepository employees=mock(EmployeeRepository.class); DepartmentRepository departments=mock(DepartmentRepository.class); RoleRepository roles=mock(RoleRepository.class);
  EmployeeService service=new EmployeeService(employees,departments,roles,new EmployeeMapper());
  when(employees.existsByEmailIgnoreCase("asha@example.com")).thenReturn(true);
  EmployeeRequest request=new EmployeeRequest("Asha","Kumar","asha@example.com",null,null,1L,Set.of());
  assertThrows(DuplicateResourceException.class,()->service.create(request)); verify(employees,never()).save(any());
 }
}

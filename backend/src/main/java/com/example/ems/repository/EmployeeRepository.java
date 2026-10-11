package com.example.ems.repository;
import com.example.ems.entity.Employee;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    boolean existsByEmailIgnoreCase(String email);
    boolean existsByEmailIgnoreCaseAndIdNot(String email, Long id);
    @EntityGraph(attributePaths = {"department", "roles"}) @Override List<Employee> findAll();
    @EntityGraph(attributePaths = {"department", "roles"}) @Override Optional<Employee> findById(Long id);
}

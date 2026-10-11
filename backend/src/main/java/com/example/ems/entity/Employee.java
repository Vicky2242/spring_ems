package com.example.ems.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "employees", uniqueConstraints = @UniqueConstraint(name = "uk_employee_email", columnNames = "email"))
public class Employee {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "first_name", nullable = false, length = 80) private String firstName;
    @Column(name = "last_name", nullable = false, length = 80) private String lastName;
    @Column(nullable = false, length = 150) private String email;
    @Column(length = 20) private String phone;
    @Column(name = "joining_date") private LocalDate joiningDate;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "department_id", nullable = false) private Department department;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "employee_roles", joinColumns = @JoinColumn(name = "employee_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new LinkedHashSet<>();
    public Employee() {}
    public Long getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public LocalDate getJoiningDate() { return joiningDate; }
    public Department getDepartment() { return department; }
    public Set<Role> getRoles() { return roles; }
    public void setFirstName(String v) { firstName = v; }
    public void setLastName(String v) { lastName = v; }
    public void setEmail(String v) { email = v; }
    public void setPhone(String v) { phone = v; }
    public void setJoiningDate(LocalDate v) { joiningDate = v; }
    public void setDepartment(Department v) { department = v; }
    public void setRoles(Set<Role> v) { roles = v; }
}

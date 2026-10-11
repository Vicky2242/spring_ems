package com.example.ems.service;
import com.example.ems.dto.DepartmentRequest;
import com.example.ems.dto.DepartmentResponse;
import com.example.ems.entity.Department;
import com.example.ems.exception.DuplicateResourceException;
import com.example.ems.exception.ResourceNotFoundException;
import com.example.ems.repository.DepartmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
@Service @Transactional
public class DepartmentService {
 private final DepartmentRepository repository;
 public DepartmentService(DepartmentRepository repository) { this.repository = repository; }
 @Transactional(readOnly=true) public List<DepartmentResponse> findAll() { return repository.findAll().stream().map(this::response).toList(); }
 public DepartmentResponse create(DepartmentRequest request) {
  String name=request.name().trim(); if(repository.existsByNameIgnoreCase(name)) throw new DuplicateResourceException("A department with this name already exists.");
  return response(repository.save(new Department(name, request.description())));
 }
 public DepartmentResponse update(Long id, DepartmentRequest request) {
  Department d=repository.findById(id).orElseThrow(()->new ResourceNotFoundException("Department not found with id: "+id));
  String name=request.name().trim(); if(repository.existsByNameIgnoreCaseAndIdNot(name,id)) throw new DuplicateResourceException("Another department already uses this name.");
  d.setName(name); d.setDescription(request.description()); return response(repository.save(d));
 }
 public void delete(Long id) { repository.delete(repository.findById(id).orElseThrow(()->new ResourceNotFoundException("Department not found with id: "+id))); }
 private DepartmentResponse response(Department d) { return new DepartmentResponse(d.getId(),d.getName(),d.getDescription()); }
}

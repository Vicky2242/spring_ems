package com.example.ems.service;
import com.example.ems.dto.RoleRequest;
import com.example.ems.dto.RoleResponse;
import com.example.ems.entity.Role;
import com.example.ems.exception.DuplicateResourceException;
import com.example.ems.exception.ResourceNotFoundException;
import com.example.ems.repository.RoleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
@Service @Transactional
public class RoleService {
 private final RoleRepository repository;
 public RoleService(RoleRepository repository) { this.repository=repository; }
 @Transactional(readOnly=true) public List<RoleResponse> findAll() { return repository.findAll().stream().map(this::response).toList(); }
 public RoleResponse create(RoleRequest request) {
  String name=request.name().trim(); if(repository.existsByNameIgnoreCase(name)) throw new DuplicateResourceException("A role with this name already exists.");
  return response(repository.save(new Role(name,request.description())));
 }
 public RoleResponse update(Long id, RoleRequest request) {
  Role r=repository.findById(id).orElseThrow(()->new ResourceNotFoundException("Role not found with id: "+id));
  String name=request.name().trim(); if(repository.existsByNameIgnoreCaseAndIdNot(name,id)) throw new DuplicateResourceException("Another role already uses this name.");
  r.setName(name); r.setDescription(request.description()); return response(repository.save(r));
 }
 public void delete(Long id) { repository.delete(repository.findById(id).orElseThrow(()->new ResourceNotFoundException("Role not found with id: "+id))); }
 private RoleResponse response(Role r) { return new RoleResponse(r.getId(),r.getName(),r.getDescription()); }
}

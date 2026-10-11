package com.example.ems.controller;
import com.example.ems.dto.RoleRequest;
import com.example.ems.dto.RoleResponse;
import com.example.ems.service.RoleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/api/roles")
public class RoleController {
 private final RoleService service;
 public RoleController(RoleService service) { this.service=service; }
 @GetMapping public List<RoleResponse> findAll() { return service.findAll(); }
 @PostMapping @ResponseStatus(HttpStatus.CREATED) public RoleResponse create(@Valid @RequestBody RoleRequest request) { return service.create(request); }
 @PutMapping("/{id}") public RoleResponse update(@PathVariable Long id,@Valid @RequestBody RoleRequest request) { return service.update(id,request); }
 @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void delete(@PathVariable Long id) { service.delete(id); }
}

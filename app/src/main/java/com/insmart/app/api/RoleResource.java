package com.insmart.app.api;

import com.insmart.app.domain.Role;
import com.insmart.app.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/role")
@SuppressWarnings("unused")
public class RoleResource {
    private final RoleService roleService;
    @GetMapping("/get")
    public ResponseEntity<List<?>> getAll(Authentication authentication, Principal principal){
        return roleService.getAll();
    }
    @GetMapping("/get/{id}")
    public ResponseEntity<?>getOne(@PathVariable Long id){
        return roleService.getOne(id);
    }
    @PostMapping("/save")
    public ResponseEntity<?> create(Authentication authentication,Principal principal,@RequestBody Role role){
        return roleService.create(role);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(Authentication authentication,Principal principal,@RequestBody Role role,@PathVariable Long id){
        return roleService.update(id,role);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(Authentication authentication,Principal principal,@PathVariable Long id){
        return roleService.delete(id);
    }
    @PostMapping("/save/{id}")
    public ResponseEntity<?> addResourceToRole(@PathVariable Long id,@RequestBody List<Long> resourceIds){
        return roleService.addResourceToRole(id,resourceIds);
    }
}

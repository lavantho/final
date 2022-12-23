package com.insmart.app.api;

import com.insmart.app.domain.Permission;
import com.insmart.app.service.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/permission")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@SuppressWarnings("unused")
public class PermissionResource {
    private final Service<Permission> permissionService;
    @GetMapping("/get")
    public ResponseEntity<List<?>> getAll(){
        return permissionService.getAll();
    }
    @GetMapping("/get/{id}")
    public ResponseEntity<?> getOne(@PathVariable Long id){
        return permissionService.getOne(id);
    }
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody Permission permission){
        return permissionService.create(permission);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable Long id,@RequestBody Permission permission){
        return permissionService.update(id,permission);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(Authentication authentication, Principal principal,@PathVariable Long id){
        return permissionService.delete(id);
    }

}

package com.insmart.app.service;
import com.insmart.app.domain.Permission;
import com.insmart.app.repo.PermissionRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;

import javax.transaction.Transactional;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@org.springframework.stereotype.Service
@Transactional(rollbackOn = RuntimeException.class)
public class PermissionService implements Service<Permission> {
    private final PermissionRepo permissionRepo;
    @Override
    public ResponseEntity<List<?>> getAll() {
        log.info("Getting all permissions");
        return ResponseEntity.ok(permissionRepo.findAll());
    }

    @Override
    public ResponseEntity<?> getOne(Long id) {
        log.info("Getting permission with id {}",id);
        return ResponseEntity.ok(permissionRepo.findById(id).orElseThrow(()->new RuntimeException("Permission not found")));
    }

    @Override
    public ResponseEntity<?> create(Permission permission) {
        log.info("Creating permission {}",permission);
        return ResponseEntity.ok(permissionRepo.save(permission));
    }

    @Override
    public ResponseEntity<?> update(Long id,Permission permission) {
        Permission permission1 = permissionRepo.findById(id).orElseThrow(()->new RuntimeException("Permission not found"));
        permission1.setDelete(permission.getDelete());
        permission1.setUpdate(permission.getUpdate());
        permission1.setRead(permission.getRead());
        permission1.setWrite(permission.getWrite());
        permission1.setResource(permission.getResource());
        log.info("Update permission {}",permission1);
        return ResponseEntity.ok(permissionRepo.save(permission1));
    }

    @Override
    public ResponseEntity<?> delete(Long id) {
        Permission permission = permissionRepo.findById(id).orElseThrow(()->new RuntimeException("Permission not found"));
        log.info("Delete permission {}",permission);
        permissionRepo.delete(permission);
        return ResponseEntity.ok(permission);
    }
}

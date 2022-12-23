package com.insmart.app.service;

import com.insmart.app.domain.Resource;
import com.insmart.app.domain.Role;
import com.insmart.app.repo.ResourceRepo;
import com.insmart.app.repo.RoleRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Slf4j@RequiredArgsConstructor
@org.springframework.stereotype.Service
@Transactional(rollbackOn = RuntimeException.class)
public class RoleService implements Service<Role> {
    private final RoleRepo roleRepo;
    private final ResourceRepo resourceRepo;
    @Override
    public ResponseEntity<List<?>> getAll() {
        log.info("Getting list role ....");
        return ResponseEntity.ok(roleRepo.findAll());
    }

    @Override
    public ResponseEntity<?> getOne(Long id) {
        log.info("get role with id {}",id);
        return ResponseEntity.ok(roleRepo.findById(id).orElseThrow(()->new RuntimeException("Role not found")));
    }

    @Override
    public ResponseEntity<?> create(Role role) {
        log.info("Create role name {} success",role.getName());
        return ResponseEntity.ok(roleRepo.save(role));
    }

    @Override
    public ResponseEntity<?> update(Long id,Role role) {
        Role role1 = roleRepo.findById(id).orElseThrow(()-> new RuntimeException("Role not found"));
        role1.setName(role.getName());
        log.info("update role id {}",id);
        try {
            log.info("update role id {} success",id);
            return ResponseEntity.ok(roleRepo.save(role1));
        }catch (Exception e){
            log.error("Update role with id {} fail !!!",id);
            return ResponseEntity.badRequest().build();
        }
    }

    @Override
    public ResponseEntity<?> delete(Long id) {
        Role role1 = roleRepo.findById(id).orElseThrow(()-> new RuntimeException("Role id not found " + id));
        log.info("Delete role id {}",id);
        roleRepo.delete(role1);
        log.info("Delete role id {} success" , id);
        return ResponseEntity.ok(role1);
    }
    public ResponseEntity<?> addResourceToRole(Long roleId, Collection<Long> resourceIds) {
        Role role = roleRepo.findById(roleId).orElseThrow(()-> new RuntimeException("Role not found"));
        Collection<Resource> resources = resourceRepo.findAllById(resourceIds);
        if(resources.isEmpty())
            return ResponseEntity.ok(Map.of("status", HttpStatus.NO_CONTENT.value(),"message","Resource not found"));
        try {
            role.getResources().addAll(resources);
            log.info("Add resource id {} to role id {} success",resourceIds,roleId);
            log.info("role {}",role.getResources());
            return ResponseEntity.ok(roleRepo.saveAndFlush(role));
        }catch (RuntimeException e){
            log.error("Add resource id {} to role id {} fail",resourceIds,roleId);
            return ResponseEntity.badRequest().build();
        }
    }
}

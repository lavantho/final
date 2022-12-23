package com.insmart.app.service;
import com.insmart.app.domain.Resource;
import com.insmart.app.repo.ResourceRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;

import javax.transaction.Transactional;
import java.util.List;

@RequiredArgsConstructor
@org.springframework.stereotype.Service
@Slf4j
@Transactional(rollbackOn = RuntimeException.class)
public class ResourceService implements Service<Resource> {
    private final ResourceRepo resourceRepo;
    @Override
    public ResponseEntity<List<?>> getAll() {
        log.info("Getting all resources");
        return ResponseEntity.ok(resourceRepo.findAll());
    }
    @Override
    public ResponseEntity<?> getOne(Long id) {
        log.info("Getting resource with id {}",id);
        return ResponseEntity.ok(resourceRepo.findById(id).orElseThrow(()->new RuntimeException("Resource not found")));
    }

    @Override
    public ResponseEntity<?> create(Resource resource) {
        log.info("Creating resource {}",resource);
        return ResponseEntity.ok(resourceRepo.save(resource));
    }

    @Override
    public ResponseEntity<?> update(Long id,Resource resource) {
        Resource resource1 = resourceRepo.findById(id).orElseThrow(()->new RuntimeException("Resource not found"));
        resource1.setCode(resource.getCode());
        resource1.setName(resource.getName());
        resource1.setUrl(resource.getUrl());
        resource1.setDescription(resource.getDescription());
        //resource1.setParent(resource.getParent());
        resource1.setResources(resource.getResources());
        log.info("Update resource {}",resource1);
        return ResponseEntity.ok(resourceRepo.save(resource1));
    }

    @Override
    public ResponseEntity<?> delete(Long id) {
        Resource resource = resourceRepo.findById(id).orElseThrow(()->new RuntimeException("Resource not found"));
        log.info("Delete resource {}",resource);
        resourceRepo.delete(resource);
        return ResponseEntity.ok(resource);
    }
}

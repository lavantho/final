package com.insmart.app.service;
import com.insmart.app.domain.Organization;
import com.insmart.app.repo.OrganizationRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;

import javax.transaction.Transactional;
import java.util.List;


@RequiredArgsConstructor
@Slf4j
@Transactional(rollbackOn = RuntimeException.class)
@org.springframework.stereotype.Service
public class OrganizationService implements Service<Organization> {
    private final OrganizationRepo organizationRepo;
    @Override
    public synchronized ResponseEntity<List<?>> getAll() {
        return ResponseEntity.ok(organizationRepo.findAll());
    }

    @Override
    public synchronized ResponseEntity<?> getOne(Long id) {
        return ResponseEntity.ok(organizationRepo.findById(id).orElseThrow(()->new RuntimeException("Organization not found")));
    }

    @Override
    public synchronized ResponseEntity<?> create(Organization organization) {
        return ResponseEntity.ok(organizationRepo.save(organization));
    }

    @Override
    public synchronized ResponseEntity<?> update(Long id,Organization organization) {
        Organization organization1 = organizationRepo.
                findById(id).orElseThrow(()->new RuntimeException("Organization not found"));
        organization1.setName(organization.getName());
        organization1.setDescription(organization.getDescription());
        log.info("Update organization {}",organization1);
        return ResponseEntity.ok(organizationRepo.save(organization1));
    }

    @Override
    public synchronized ResponseEntity<?> delete(Long id) {
        Organization organization = organizationRepo.findById(id).orElseThrow(()->new RuntimeException("Organization not found"));
        organizationRepo.delete(organization);
        log.info("Delete organization {}",organization);
        return ResponseEntity.ok(organization);
    }
}

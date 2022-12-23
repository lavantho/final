package com.insmart.app.repo;

import com.insmart.app.domain.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationRepo extends JpaRepository<Organization,Long> {
}

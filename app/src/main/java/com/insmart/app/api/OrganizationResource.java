package com.insmart.app.api;
import com.insmart.app.domain.Organization;
import com.insmart.app.service.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/organization")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@SuppressWarnings("unused")
public class OrganizationResource {
    private final Service<Organization> organizationService;
    @GetMapping("/get")
    public ResponseEntity<List<?>> getAll(){
        return organizationService.getAll();
    }
    @GetMapping("/get/{id}")
    public ResponseEntity<?> getOne(@PathVariable Long id){
        return organizationService.getOne(id);
    }
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody Organization organization){
        return organizationService.create(organization);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable Long id,@RequestBody Organization organization){
        return organizationService.update(id,organization);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        return organizationService.delete(id);
    }

}

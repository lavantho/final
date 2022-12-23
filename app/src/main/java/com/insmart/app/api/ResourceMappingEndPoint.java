package com.insmart.app.api;

import com.insmart.app.domain.Resource;
import com.insmart.app.service.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/resource")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@SuppressWarnings("unused")
public class ResourceMappingEndPoint {
    private final Service<Resource> resourceService;
    @GetMapping("/get")
    public ResponseEntity<List<?>> getAll(){
        return resourceService.getAll();
    }
    @GetMapping("/get/{id}")
    public ResponseEntity<?> getOne(@PathVariable Long id){
        return resourceService.getOne(id);
    }
    @PostMapping("/save")
    public ResponseEntity<?> create(@RequestBody Resource resource){
        return resourceService.create(resource);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable Long id,@RequestBody Resource resource){
        return resourceService.update(id,resource);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        return resourceService.delete(id);
    }
}

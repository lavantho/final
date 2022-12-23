package com.insmart.app.api;

import com.insmart.app.domain.Role;
import com.insmart.app.domain.User;
import com.insmart.app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
@CrossOrigin(origins = "*")
@SuppressWarnings("unused")
public class UserResource {
    private final UserService userService;
    @GetMapping("/get")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userService.findAll());
    }
    @PostMapping("/save")
    public ResponseEntity<User> save (@RequestBody User user) throws IOException {
        return ResponseEntity.ok(userService.save(user));
    }
    @GetMapping("/user/{username}")
    public ResponseEntity<User> findByUsername (@PathVariable String username){
        return ResponseEntity.ok(userService.findByUsername(username));
    }
    @PostMapping("/add-role/save")
    public ResponseEntity<?> addRoleToUser (@RequestParam String username,@RequestParam String roleName){
        userService.addRoleToUser(username, roleName);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/role/save")
    public ResponseEntity<Role> save (@RequestBody Role role){
        return ResponseEntity.ok(userService.save(role));
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete (@PathVariable Long id){
        userService.delete(id);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
       userService.refreshToken(request, response);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<User> update (@RequestBody User user,@PathVariable Long id){
        return ResponseEntity.ok(userService.update(user, id));
    }
}

package com.insmart.app.service;

import com.insmart.app.domain.Role;
import com.insmart.app.domain.User;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface UserService {
    User save(User user) throws IOException;
    Role save(Role role);
    void addRoleToUser(String username, String roleName);
    User findByUsername(String username);
    List<User> findAll();
    void refreshToken(HttpServletRequest request, HttpServletResponse response)throws IOException;
    void delete(Long id);
    User update(User user,Long id);
}

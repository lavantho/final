package com.insmart.app.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.insmart.app.domain.Role;
import com.insmart.app.domain.User;
import com.insmart.app.repo.RoleRepo;
import com.insmart.app.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(rollbackOn = RuntimeException.class)
@Slf4j
@SuppressWarnings("unused")
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final PasswordEncoder passwordEncoder;
    private final ServerSocket serverSocket;
    @Override
    public User save(User user) throws IOException {
        Socket socket = serverSocket.accept();
        log.info("Saving new user {} to the database", user.getName());
        log.info("Password {} encoded", user.getPassword());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        log.info("connected {}",socket.isConnected());
        if(userRepo.save(user) != null){

            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            dataOutputStream.writeUTF("User " + user.getName() + " has been created");
            dataOutputStream.flush();
            dataOutputStream.close();
            socket.close();
        }
        return user;
    }

    @Override
    public Role save(Role role) {
        log.info("Saving new role {} to the database", role.getName());
        return roleRepo.save(role);
    }

    @Override
    public void addRoleToUser(String username, String roleName) {
        log.info("Adding role {} to user {}", roleName, username);
        User user = userRepo.findByUsername(username);
        Role role = roleRepo.findByName(roleName);
        user.getRoles().add(role);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<GrantedAuthority> updatedAuthorities = new ArrayList<>(auth.getAuthorities());
        updatedAuthorities.add(new SimpleGrantedAuthority(roleName));
        Authentication newAuth = new UsernamePasswordAuthenticationToken(auth.getPrincipal(), auth.getCredentials(), updatedAuthorities);
        SecurityContextHolder.getContext().setAuthentication(newAuth);
        log.info("Role {} " ,newAuth.getAuthorities());

    }

    @Override
    public User findByUsername(String username) {
        log.info("Fetching user {}", username);
        return userRepo.findByUsername(username);
    }

    @Override
    public List<User> findAll() {
        log.info("Fetching all users");
        return userRepo.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username);
        if (user == null) {
            log.error("User not found in the database");
            throw new UsernameNotFoundException("User not found in the database");
        }else
            log.info("User found in the database: {}", username);
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }
    @Override
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader("Authorization");
        // get Claim from token
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String refreshToken = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256("vHPxbmyhXq".getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refreshToken);
                    if(decodedJWT.getExpiresAt().getTime() + 500_000 > System.currentTimeMillis()){
                        String username = decodedJWT.getSubject();
                        User user =userRepo.findByUsername(username);
                        String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
                        String accessToken = JWT.create()
                                .withSubject(user.getUsername())
                                .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                                .withIssuer(request.getRequestURL().toString())
                                .withClaim("roles", user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                                .sign(algorithm);
                        Map<String, String> tokens = Map.of("access_token",accessToken,"refresh_token",refreshToken);
                        response.setContentType("application/json");
                        log.info("Access token and refresh token generated");
                        new ObjectMapper().writeValue(response.getOutputStream(),tokens);
                    }
            }catch (Exception e) {
                log.error("Error refresh token in: {}", e.getMessage());
                response.setHeader("error", e.getMessage());
                Map<String, String> error = Map.of("error_message", e.getMessage());
                response.setContentType("application/json");
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        } else {
         log.error("Refresh token is missing");
        }
    }
    @Override
    public void delete(Long id){
        log.info("Deleting user with id : {}", id);
        userRepo.deleteById(id);
        log.info("User with id : {} deleted", id);
    }

    @Override
    public User update(User user, Long id) {
        User user1 = userRepo.findById(id).orElseThrow(()-> new RuntimeException("Not found user id " + id));
        user1.setName(user.getName());
        user1.setPassword(passwordEncoder.encode(user.getPassword()));
        user1.setRoles(user.getRoles());
        log.info("User with id : {} updated", id);
        return userRepo.save(user1);
    }
}

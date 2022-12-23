package com.insmart.app.filter;

import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import com.auth0.jwt.JWT;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.MimeTypeUtils;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;
@SuppressWarnings("unused")
@Slf4j
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    public CustomAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            Scanner sc = new Scanner(request.getReader());
            StringBuilder sb = new StringBuilder();
            while (sc.hasNext()) {
                sb.append(sc.nextLine());
            }
            JSONObject jsonObject = new JSONObject(sb.toString());
            String username = jsonObject.getString("username");
            String password = jsonObject.getString("password");
            log.info("Username is: {}", username);
            log.info("Password is: {}", password);
            UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken(username, password);
            return authenticationManager.authenticate(authReq);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        User user = (User) authResult.getPrincipal();
        log.info("User {} successfully authenticated", user.getUsername());
        Algorithm algorithm = Algorithm.HMAC256("vHPxbmyhXq".getBytes(StandardCharsets.UTF_8));
        String accessToken = JWT.create().
                                withSubject(user.getUsername()).
                                withExpiresAt(new java.util.Date(System.currentTimeMillis() + 10 * 60 * 1000)).
                                withIssuer(request.getRequestURL().toString()).
                                withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList())).
                        //        withClaim("authorities",list).
                                sign(algorithm);
        String refreshToken = JWT.create().
                withSubject(user.getUsername()).
                withExpiresAt(new java.util.Date(System.currentTimeMillis() + 30 * 60 * 1000)).
                withIssuer(request.getRequestURL().toString()).
                withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList())).
                        sign(algorithm);
        response.setHeader("refresh_token", refreshToken);
        response.setHeader("Authorization",accessToken);
        Map<String,String> tokens = Map.of("access_token",accessToken,"refresh_token",refreshToken);
        response.setContentType(MimeTypeUtils.APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(),tokens);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        super.unsuccessfulAuthentication(request, response, failed);
    }
}

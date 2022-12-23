package com.insmart.app.service.security;

import com.insmart.app.filter.CustomAuthenticationFilter;
import com.insmart.app.filter.CustomAuthorizationFilter;
import com.insmart.app.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import java.util.Collections;
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
@SuppressWarnings("unused")
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserDetailsService userDetailsService;
    private final UserService userService;
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
       auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManagerBean());
        customAuthenticationFilter.setFilterProcessesUrl("/api/login");
        http.cors().and().csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authorizeRequests().antMatchers("/api/login","/api/user/token/**").permitAll();
        int i = 0;
        userService.findAll().forEach(user -> user.getRoles().forEach(
                role -> role.getResources().forEach(resource -> {
                    try {
                        log.info("Resource is {} and role is {}" ,resource.getUrl(),role.getName());
                        http.authorizeRequests().antMatchers(resource.getUrl()).hasAuthority(role.getName());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
        ));
//        http.authorizeRequests().antMatchers("/api/user/get","/api/user/update/**","/api/user/delete/**","/api/user/user/**").hasAuthority("ROLE_ADMIN");
//        http.authorizeRequests().antMatchers("/api/user/add-role/**").hasAnyAuthority("ROLE_ADMIN","ROLE_USER");
//        http.authorizeRequests().antMatchers("/api/permission/**","/api/resource/**").hasAnyAuthority("ROLE_ADMIN","ROLE_USER");
        http.authorizeRequests().anyRequest().permitAll();
        http.addFilter(customAuthenticationFilter);
        http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOriginPatterns(Collections.singletonList("*"));
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new org.springframework.web.filter.CorsFilter(source);
    }
}

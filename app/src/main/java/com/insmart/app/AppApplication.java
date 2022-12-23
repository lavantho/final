package com.insmart.app;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import javax.transaction.Transactional;
import java.io.IOException;
import java.net.ServerSocket;


@SpringBootApplication
@SuppressWarnings("unused")
@Transactional
@Slf4j
public class AppApplication {
	@Value("${socket.port}")
	private Integer port;
	public static void main(String[] args) {
		SpringApplication.run(AppApplication.class, args);
	}
	@Bean
	public PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}
	@Bean
	public ServerSocket createServerSocket()throws IOException {
		log.info("Socket port: {} started",port);
		return new ServerSocket(port);
	}
	/*
	This is init method
	method will be called when the application is started
	method will be created only for testing purposes
	 */
//	@Bean
//	CommandLineRunner run(UserService userService, ResourceRepo resourceRepo, PermissionRepo permissionRepo, RoleRepo roleRepo) {
//		return args -> {
//			userService.save(new Role(null, "ROLE_USER"));
//			userService.save(new Role(null, "ROLE_ADMIN"));
//			//userService.save(new User(null, "lã văn thọ", "john","1234","tholv@ftl.vn","ADMIN","", new ArrayList<>(),null));
//			userService.save(new User(null, "adminstrator", "admin","1234","tholv@ftl.vn","ADMIN1","", new ArrayList<>(),null));
//			// userService.addRoleToUser("john", "ROLE_USER");
//			userService.addRoleToUser("admin", "ROLE_ADMIN");
//			Resource resource1 = new Resource(null, "1", "resource", "/admin/resource-manager",null, null);
//			resourceRepo.save(resource1);
//			Resource resource2 = new Resource(null, "123", "user", "/admin/user-manager",null, null);
//			resourceRepo.save(resource2);
//			//Permission permission  = new Permission(null,userService.findByUsername("john"),resource1,true,true,true,true);
//			Permission permission1  = new Permission(null,userService.findByUsername("admin"),resource1,false,true,true,true);
//			//permissionRepo.save(permission);
//			permissionRepo.save(permission1);
//		};
//	}

}


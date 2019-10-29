package com.example.curso.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.curso.entities.Role;
import com.example.curso.entities.User;
import com.example.curso.repositories.RoleRepository;
import com.example.curso.repositories.UserRepository;

@Configuration
@Profile("test")
public class TestConfig implements CommandLineRunner {

	@Autowired
	private BCryptPasswordEncoder passwordEncode;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Override
	public void run(String... args) throws Exception {
				
		User u1 = new User(null, "Maria Brown", "maria@gmail.com", "988888888", passwordEncode.encode("123456"));
		User u2 = new User(null, "Alex Green", "alex@gmail.com", "977777777", passwordEncode.encode("123456"));	
	
		userRepository.saveAll(Arrays.asList(u1, u2));
		
		Role r1 = new Role(null, "ROLE_MEMBER");
		Role r2 = new Role(null, "ROLE_ADMIN");
		
		roleRepository.saveAll(Arrays.asList(r1, r2));
		
		u1.getRoles().add(r1);
		u2.getRoles().add(r1);
		u2.getRoles().add(r2);
		
		userRepository.saveAll(Arrays.asList(u1, u2));
	}
}

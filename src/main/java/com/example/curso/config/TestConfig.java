package com.example.curso.config;

import java.time.Instant;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.curso.entities.Comment;
import com.example.curso.entities.Post;
import com.example.curso.entities.Role;
import com.example.curso.entities.User;
import com.example.curso.repositories.CommentRepository;
import com.example.curso.repositories.PostRepository;
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

	@Autowired
	private CommentRepository commentRepository;

	@Autowired
	private PostRepository postRepository;

	@Override
	public void run(String... args) throws Exception {

		User u1 = new User(null, "Maria Brown", "maria@gmail.com", "988888888", passwordEncode.encode("123456"));
		User u2 = new User(null, "Alex Green", "alex@gmail.com", "977777777", passwordEncode.encode("123456"));
		User u3 = new User(null, "junior", "junior@gmail.com", "977333000", passwordEncode.encode("123456"));

		userRepository.saveAll(Arrays.asList(u1, u2, u3));

		Role r1 = new Role(null, "ROLE_MEMBER");
		Role r2 = new Role(null, "ROLE_ADMIN");

		roleRepository.saveAll(Arrays.asList(r1, r2));

		u1.getRoles().add(r1);
		u2.getRoles().add(r1);
		u2.getRoles().add(r2);
		u3.getRoles().add(r1);

		userRepository.saveAll(Arrays.asList(u1, u2, u3));

		// Category cat1 = new Category(null, "Electronics");
		Post pos1 = new Post(null, "Hello World", Instant.parse("2019-06-20T19:53:07Z"),
				"First post testing the whole thing", u1);
		Post pos2 = new Post(null, "Keep trying", Instant.parse("2019-06-22T17:11:00Z"),
				"Im still learning how to post", u1);
		Post pos3 = new Post(null, "Flamengo is the best", Instant.parse("2019-05-30T11:10:22Z"),
				"Follow the leader losers", u2);
		Post pos4 = new Post(null, "Implement Test", Instant.parse("2019-06-03T02:00:00Z"),
				"Validating the post for user 3", u3);

		postRepository.saveAll(Arrays.asList(pos1, pos2, pos3, pos4));

		Comment com1 = new Comment(null, "Welcome to this new world", Instant.parse("2019-06-20T19:55:00Z"), pos1, u2);
		Comment com2 = new Comment(null, "You are doing a good job", Instant.parse("2019-06-22T17:12:00Z"), pos2, u2);
		Comment com3 = new Comment(null, "Vamos Mengooooo", Instant.parse("2019-05-30T11:10:25Z"), pos3, u1);
		Comment com4 = new Comment(null, "I can see you are learning well", Instant.parse("2019-06-12T02:00:00Z"), pos4,
				u1);

		commentRepository.saveAll(Arrays.asList(com1, com2, com3, com4));

	}
}

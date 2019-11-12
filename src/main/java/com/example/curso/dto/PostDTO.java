package com.example.curso.dto;

import java.io.Serializable;
import java.time.Instant;

import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

import com.example.curso.entities.Post;
import com.example.curso.entities.User;
import com.example.curso.services.validation.UserUpdateValid;

@UserUpdateValid
public class PostDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private Instant instante = Instant.now();
	
	@NotEmpty(message = "can't be empty")
	@Length(min = 5, max = 80, message = "length must be between 5 and 80")
	private String title;
	
	@NotEmpty(message = "can't be empty")
	private String body;
	
	@NotEmpty(message = "can't be empty")
	private String phone;
		
	private String name;
	
	public PostDTO() {
		
	}

	public PostDTO(Long id, String title, String body, String name) {
		super();
		this.id = id;
		this.title = title;
		this.body = body;
		this.name = name;
	}
	
	public PostDTO(Post entity) {
		this.id = entity.getId();
		this.title = entity.getTitle();
		this.body = entity.getBody();
		this.instante = entity.getInstante();
		this.name = entity.getName();
			
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}
	
	public Post toEntity() {
		return new Post(id, title, body, name);
	}
	
}

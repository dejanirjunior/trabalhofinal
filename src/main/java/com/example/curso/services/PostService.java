package com.example.curso.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.userdetails.PostDetails;
import org.springframework.security.core.userdetails.PostDetailsService;
import org.springframework.security.core.userdetails.PostnameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.curso.dto.PostDTO;
import com.example.curso.dto.PostInsertDTO;
import com.example.curso.entities.Post;
import com.example.curso.repositories.PostRepository;
import com.example.curso.services.exceptions.DatabaseException;
import com.example.curso.services.exceptions.ResourceNotFoundException;


@Service
public class PostService implements PostDetailsService {

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private PostRepository repository;
	
	@Autowired
	private AuthService authService;
	
	public List<PostDTO> findAll() {
	List<Post> list = repository.findAll();
	return list.stream().map(e -> new PostDTO(e)).collect(Collectors.toList());
		}
	
	public PostDTO findById(Long id) {
		authService.validateSelfOrAdmin(id);
		Optional<Post> obj = repository.findById(id);
		Post entity = obj.orElseThrow(() -> new ResourceNotFoundException(id));
		return new PostDTO(entity);
	}
	
	public PostDTO insert(PostInsertDTO dto) {
		Post entity = dto.toEntity();
		entity.setPassword(passwordEncoder.encode(dto.getPassword()));
		entity = repository.save(entity);
		return new PostDTO(entity);
	}
	
	public void delete(Long id) {
		try {
			repository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException(id);
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException(e.getMessage());
		}
	}
	@Transactional
	public PostDTO update(Long id, PostDTO dto) {
		authService.validateSelfOrAdmin(id);
		try {
		Post entity = repository.getOne(id);
		updateData(entity, dto);
		entity = repository.save(entity);
		return new PostDTO(entity);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException(id);
		}
	}

	private void updateData(Post entity, PostDTO dto) {
		entity.setName(dto.getName());
		entity.setEmail(dto.getEmail());
		entity.setPhone(dto.getPhone());
		
	}

	@Override
	public PostDetails loadPostByPostname(String username) throws PostnameNotFoundException {
		Post user = repository.findByEmail(username);
		if (user == null) {
			throw new PostnameNotFoundException(username);
		}
		return user;
	}
	
}
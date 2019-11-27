package com.example.curso.services;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.curso.dto.CommentDTO;
import com.example.curso.dto.PostDTO;
import com.example.curso.entities.Comment;
import com.example.curso.entities.Post;
import com.example.curso.entities.User;
import com.example.curso.repositories.PostRepository;
import com.example.curso.repositories.UserRepository;
import com.example.curso.services.exceptions.DatabaseException;
import com.example.curso.services.exceptions.ResourceNotFoundException;


@Service
public class PostService {

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private PostRepository repository;
	
	@Autowired
	private AuthService authService;
	
	@Autowired
	private UserRepository userRepository;
	
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
		entity.setTitle(dto.getTitle());
		entity.setBody(dto.getBody());
	}
		

	@Transactional
	public PostDTO insert(@Valid PostDTO dto) {
		User author = authService.authenticated();
		Post post = dto.toEntity();
		post.setAuthor(author);
		post.setInstante(Instant.now());
		post = repository.save(post);
		return new PostDTO(post);
	}

	public Page<PostDTO> myTimeLine(Pageable pageable) {
		User author = authService.authenticated();
		Page<Post> posts = repository.findByAuthor(author, pageable);
		return posts.map(e -> new PostDTO(e));
	}

	public Page<PostDTO> timeLine(Long userId, Pageable pageable) {
		User author = userRepository.getOne(userId);
		Page<Post> posts = repository.findByAuthor(author, pageable);
		return posts.map(e -> new PostDTO(e));
	}
	
	@Transactional(readOnly = true)
	public List<CommentDTO> findComment(Long id) {
		Post post = repository.getOne(id);
		Set<Comment> set = post.getComments();
		return set.stream().map(e -> new CommentDTO(e)).collect(Collectors.toList());
	}
	
    public Page<PostDTO> findByBodyPaged(String body, Pageable pageable) {
        Page<Post> list;
        list = repository.findByBodyContainingIgnoreCase(body, pageable);
        return list.map(e -> new PostDTO(e));
    }
}
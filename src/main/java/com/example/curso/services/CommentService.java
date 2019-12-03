package com.example.curso.services;

import java.time.Instant;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.curso.dto.CommentDTO;
import com.example.curso.entities.Comment;
import com.example.curso.entities.User;
import com.example.curso.repositories.CommentRepository;
import com.example.curso.services.exceptions.DatabaseException;
import com.example.curso.services.exceptions.ResourceNotFoundException;

@Service
public class CommentService {

    @Autowired
    private AuthService authService;

    @Autowired
    private CommentRepository repository;

    @Transactional
    public CommentDTO insert(@Valid CommentDTO dto) {
        User author = authService.authenticated();
        Comment comment = dto.toEntity();
        comment.setAuthor(author);
        comment.setInstante(Instant.now());
        comment = repository.save(comment);

        return new CommentDTO(comment);
    }

    public void delete(Long id) {
    	authService.validateSelfOrAdmin(id);
        try {
            repository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    @Transactional
    public CommentDTO update(Long id, CommentDTO dto) {
        authService.validateSelfOrAdmin(id);
        try {
            Comment entity = repository.getOne(id);
            updateData(entity, dto);
            entity = repository.save(entity);
            return new CommentDTO(entity);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(id);
        }
    }

    private void updateData(Comment entity, CommentDTO dto) {
        entity.setText(dto.getText());
        entity.setInstante(Instant.now());
    }

    public Page<CommentDTO> findByTextPaged(String text, Pageable pageable) {
        Page<Comment> list;

        list = repository.findByTextContainingIgnoreCase(text, pageable);

        return list.map(e -> new CommentDTO(e));
    }
    
}

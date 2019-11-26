package com.example.curso.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.curso.entities.Comment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Transactional(readOnly = true)
    @Query("SELECT obj FROM Comment obj WHERE LOWER(obj.text) LIKE LOWER(CONCAT('%',:text,'%'))")
    Page<Comment> findByTextContainingIgnoreCase(String text, Pageable pageable);

}

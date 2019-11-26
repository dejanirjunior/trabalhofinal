package com.example.curso.repositories;

import com.example.curso.entities.Comment;
import com.example.curso.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.curso.entities.Post;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Transactional(readOnly = true)
    @Query("SELECT obj FROM Post obj WHERE LOWER(obj.name) LIKE LOWER(CONCAT('%',:name,'%'))")
    Page<Post> findByNameContainingIgnoreCase(String name, Pageable pageable);

    @Transactional(readOnly = true)
    @Query("SELECT obj FROM Post obj INNER JOIN obj.users users WHERE :user IN users")
    Page<Post> findByUser(User user, Pageable pageable);

    @Transactional(readOnly = true)
    @Query("SELECT obj FROM Comment obj INNER JOIN obj.posts psts WHERE psts.id = :id")
    Page<Comment> findCommentsContainingIgnoreCase(Long id, Pageable pageable);
}

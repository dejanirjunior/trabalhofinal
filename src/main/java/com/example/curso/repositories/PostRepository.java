package com.example.curso.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.example.curso.entities.Post;
import com.example.curso.entities.User;

public interface PostRepository extends JpaRepository<Post, Long> {

	@Transactional(readOnly = true)
    @Query("SELECT obj FROM Post obj WHERE obj.author = :author")
	Page<Post> findByAuthor(@Param("author") User author, Pageable pageable);

   
}

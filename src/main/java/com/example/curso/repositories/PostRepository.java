package com.example.curso.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.curso.entities.Post;

public interface PostRepository extends JpaRepository<Post, Long> {

}

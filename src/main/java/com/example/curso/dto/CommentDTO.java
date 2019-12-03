package com.example.curso.dto;

import com.example.curso.entities.Comment;
import com.example.curso.entities.Post;
import com.example.curso.entities.User;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.time.Instant;

public class CommentDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private Instant moment;
    private String author;
    private Long authorId;
    private String postTitle;
    private Long postId;


    @NotEmpty(message = "can't be empty")
    private String text;

    public CommentDTO(Comment entity) {
        this.id = entity.getId();
        this.moment = entity.getInstante();
        this.text = entity.getText();
        this.author = entity.getAuthor().getName();
        this.authorId = entity.getAuthor().getId();
        this.postTitle = entity.getPost().getTitle();
        this.postId = entity.getPost().getId();
    }

    public CommentDTO() {
    	
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getMoment() {
        return moment;
    }

    public void setMoment(Instant moment) {
        this.moment = moment;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Comment toEntity() {
        User userAuthor = new User(authorId, author, null, null, null);
        Post post = new Post(postId, postTitle, moment, null, userAuthor);

        return new Comment(id, text, moment, post, userAuthor);
    }
}

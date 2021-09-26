package com.example.restapiclient.service;

import com.example.restapiclient.config.CommentsRestClientConfig;
import com.example.restapiclient.errorhandler.CommentErrorHandler;
import com.example.restapiclient.model.Comment;
import com.example.restapiclient.repository.CommentsRestClientRepository;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Repository
public class CommentRestClientService implements CommentsRestClientRepository {

    private final CommentsRestClientConfig commentConfig;
    private final CommentErrorHandler errorHandler;

    public CommentRestClientService(CommentsRestClientConfig commentConfig, CommentErrorHandler errorHandler) {
        this.commentConfig = commentConfig;
        this.errorHandler = errorHandler;
    }

    @Override
    public List<Comment> getAllComments() {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(commentConfig.getBaseUrl(), List.class);
    }

    @Override
    public Optional<Comment> getCommentById(long id) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(errorHandler);
        Comment comment = restTemplate.getForObject(commentConfig.getBaseUrl() + "/" + id, Comment.class);
        return Optional.of(comment);
    }

    @Override
    public List<Comment> getCommentsByPostId(long id) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(errorHandler);
        return restTemplate.getForObject(commentConfig.getBaseUrl() + "?postId=" + id, List.class);
    }

    @Override
    public Comment addComment(Comment comment) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(errorHandler);
        Comment comment1 = restTemplate.postForObject(commentConfig.getBaseUrl() + "/", comment, Comment.class);
        return comment1;
    }


    @Override
    public Comment deleteComment(long id) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(errorHandler);
        System.out.println(id+"silindi");
        ResponseEntity<Comment> commentResponseEntity = restTemplate.exchange(commentConfig.getBaseUrl() + "/" + id,
                HttpMethod.DELETE,HttpEntity.EMPTY,Comment.class);
        return commentResponseEntity.getBody();
    }

    @Override
    public Comment updateComment(long id,Comment comment) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(errorHandler);
        System.out.println(id+"upddate oldu");
        HttpEntity<Comment> commentHttpEntity=new HttpEntity<>(comment);
        ResponseEntity<Comment> commentResponseEntity = restTemplate.exchange(commentConfig.getBaseUrl() + "/" + id,
                HttpMethod.PUT,commentHttpEntity,Comment.class);
        return commentResponseEntity.getBody();
    }

}

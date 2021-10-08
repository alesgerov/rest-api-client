package com.example.restapiclient.service;

import com.example.restapiclient.config.RestClientConfig;
import com.example.restapiclient.errorhandler.ClientErrorHandler;
import com.example.restapiclient.errorhandler.ResourceNotFound;
import com.example.restapiclient.model.Comment;
import com.example.restapiclient.model.Message;
import com.example.restapiclient.model.ResponseForm;
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

    private final RestClientConfig commentConfig;
    private final ClientErrorHandler errorHandler;

    public CommentRestClientService(RestClientConfig commentConfig, ClientErrorHandler errorHandler) {
        this.commentConfig = commentConfig;
        this.errorHandler = errorHandler;
    }

    @Override
    public List<Comment> getAllComments() {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(commentConfig.getCommentsBaseUrl(), List.class);
    }

    @Override
    public Optional<Comment> getCommentById(long id) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(errorHandler);
        Comment comment = restTemplate.getForObject(commentConfig.getCommentsBaseUrl() + "/" + id, Comment.class);
        return Optional.of(comment);
    }

    @Override
    public List<Comment> getCommentsByPostId(long id) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(errorHandler);
        return restTemplate.getForObject(commentConfig.getCommentsBaseUrl() + "?postId=" + id, List.class);
    }

    @Override
    public Comment addComment(Comment comment) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(errorHandler);
        Comment comment1 = restTemplate.postForObject(commentConfig.getCommentsBaseUrl() + "/", comment, Comment.class);
        return comment1;
    }


    @Override
    public ResponseForm deleteComment(long id) {
        RestTemplate restTemplate = new RestTemplate();
        if (getCommentById(id).isEmpty()) throw new ResourceNotFound(Message.commentNotFound);
        restTemplate.setErrorHandler(errorHandler);
        restTemplate.exchange(commentConfig.getCommentsBaseUrl() + "/" + id,
                HttpMethod.DELETE, HttpEntity.EMPTY, Comment.class);
        return new ResponseForm(Message.commentDeleted);
    }

    @Override
    public Comment updateComment(long id, Comment comment) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(errorHandler);
        System.out.println(id + "upddate oldu");
        HttpEntity<Comment> commentHttpEntity = new HttpEntity<>(comment);
        ResponseEntity<Comment> commentResponseEntity = restTemplate.exchange(commentConfig.getCommentsBaseUrl() + "/" + id,
                HttpMethod.PUT, commentHttpEntity, Comment.class);
        return commentResponseEntity.getBody();
    }

}

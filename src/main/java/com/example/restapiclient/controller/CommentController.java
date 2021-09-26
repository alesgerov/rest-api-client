package com.example.restapiclient.controller;


import com.example.restapiclient.errorhandler.ResourceNotFound;
import com.example.restapiclient.model.Comment;
import com.example.restapiclient.service.CommentRestClientService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RequestMapping(value = {"/client/api/v1/comments", "/client/api/v1/comments/"})
@RestController
public class CommentController {

    private final CommentRestClientService commentService;

    public CommentController(CommentRestClientService commentService) {
        this.commentService = commentService;
    }


    @GetMapping
    public List<Comment> getAllComments() {
        return commentService.getAllComments();
    }


    @GetMapping(value = {"/{id}", "/{id}/"})
    public Comment getCommentById(@PathVariable("id") long id) {
        try {
            Optional<Comment> optionalComment = commentService.getCommentById(id);
            return optionalComment.get();
        } catch (ResourceNotFound e) {
            System.out.println(e.getMessage());
            return null;
        }

    }

    @GetMapping(value = {"/post/{id}", "/post/{id}/"})
    public List<Comment> getCommentsByPostId(@PathVariable("id") long id) {
        return commentService.getCommentsByPostId(id);
    }
}

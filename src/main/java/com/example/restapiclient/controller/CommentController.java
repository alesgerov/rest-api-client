package com.example.restapiclient.controller;


import com.example.restapiclient.errorhandler.ResourceNotFound;
import com.example.restapiclient.model.Comment;
import com.example.restapiclient.service.CommentRestClientService;
import org.springframework.web.bind.annotation.*;

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


    @PostMapping(value = {"/add", "/add/"})
    public Comment addComment(@RequestBody Comment comment) {
        return commentService.addComment(comment);
    }



    @DeleteMapping(value = {"/{id}","/{id}/"})
    public Comment deleteComment(@PathVariable("id") long id){
        return commentService.deleteComment(id);
    }
}

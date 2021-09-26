package com.example.restapiclient.controller;


import com.example.restapiclient.model.Comment;
import com.example.restapiclient.service.CommentRestClientService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RequestMapping(value = {"/client/api/v1/comments","/client/api/v1/comments/"})
@RestController
public class CommentController {

    private final CommentRestClientService commentService;

    public CommentController(CommentRestClientService commentService) {
        this.commentService = commentService;
    }


    @GetMapping
    public List<Comment> getAllComments(){
        return commentService.getAllComments();
    }


    @GetMapping(value = {"/{id}","/{id}/"})
    public Comment getCommentById(@PathVariable("id") long id){
        Optional<Comment> optionalComment=commentService.getCommentById(id);
        if (optionalComment.isPresent()){
            return optionalComment.get();
        }else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"false id");
        }
    }
}
